import {Component, OnInit} from '@angular/core'
import {EventDetail} from "../../model/event.model"
import OLMap from "ol/Map"
import {Feature, MapBrowserEvent, View} from "ol"
import {fromLonLat, toLonLat} from "ol/proj"
import {apply} from "ol-mapbox-style"
import {environment} from "../../../environments/environment"
import VectorLayer from "ol/layer/Vector"
import VectorSource from "ol/source/Vector"
import {Geometry, Point} from "ol/geom"
import {Cluster, Source} from "ol/source"
import {Icon, Style} from "ol/style"
import {ProgressSpinner} from "primeng/progressspinner";
import {CommonModule} from "@angular/common";
import {Store} from "@ngrx/store";
import {mapActions, mapFeature, MapState} from "../../core/map-store";
import {LonLat} from "../../model/lonlat.model";
import {Router} from "@angular/router";
import {EventService} from "../../core/service/event.service";
import {combineLatestWith, from, map} from "rxjs";
import {FeatureLike} from "ol/Feature";
import {Layer} from "ol/layer";
import LayerRenderer from "ol/renderer/Layer";

@Component({
    templateUrl: "./event-map.component.html",
    selector: "sk-events-map",
    styleUrls: ["./event-map.component.scss"],
    imports: [
        CommonModule,
        ProgressSpinner
    ],
    standalone: true
})
export class EventMapComponent implements OnInit {
    readonly mapId = "newsFactsMap"
    readonly initialZoom = 1
    readonly defaultMapCenterLonLat = [3.162845, 46.990896]

    //minimum distance allowed between markers before grouping into cluster
    readonly clusterMinDistance = 32
    //distance around markers to detect click
    readonly markerHitTolerance = 3
    //duration of map animations triggered when changing page or clicking on trainer group markers
    readonly mapAnimationDuration = 1000

    readonly baseMarkerPath = "/assets/map-markers/unselected.png"
    readonly selectedMarkerPath = "/assets/map-markers/selected.png"
    readonly groupMarkerPath = "/assets/map-markers/group.png"

    isSelectingLocation: boolean;

    map: OLMap
    isReadyMap: boolean = false
    eventMarkersById!: { [id: string]: Feature<Point> }
    selectedEventId: string | null = null

    eventMarkersLayer: VectorLayer
    eventMarkersSource: VectorSource
    clusteredEventSource: Cluster

    constructor(
        private mapStore: Store<MapState>,
        private router: Router,
        private eventService: EventService
    ) {
    }

    ngOnInit(): void {
        this.initEmptyMarkerLayer()
        this.mapStore.select(mapFeature.selectSelectingLocation).subscribe(selectResult => {
            this.isSelectingLocation = selectResult
        })
        this.mapStore.select(mapFeature.selectEvents).subscribe(events => {
            if (events.length) {
                this.displayEvents(events)
            }
        })

        let unstyledMap = new OLMap({
            view: new View({
                constrainResolution: true,
                center: fromLonLat(this.defaultMapCenterLonLat),
                zoom: this.initialZoom,
            })
        })
        this.eventService.list().pipe(
            combineLatestWith(from(apply(unstyledMap, environment.mapStyleLink)).pipe(
                map(styledMapOrLayer => styledMapOrLayer as OLMap)
            ))
        ).subscribe(([events, styledMap]) => {
            this.map = styledMap
            this.map.setTarget(this.mapId)
            this.map.addLayer(this.eventMarkersLayer)
            this.initMapInteractions()
            this.mapStore.dispatch(mapActions.setEvents({events: events}))
            this.isReadyMap = true
        })
    }

    private initMapInteractions() {
        this.map.on("click", (mapClickEvent: MapBrowserEvent) => {
            if (this.isSelectingLocation) {
                let olLonLat = toLonLat(mapClickEvent.coordinate);
                let location: LonLat = {
                    longitude: olLonLat[0],
                    latitude: olLonLat[1]
                }
                this.mapStore.dispatch(mapActions.setLocationForEventCreation({location: location}))
                this.router.navigate(["add-event"])
            } else {
                this.map.forEachFeatureAtPixel(
                    mapClickEvent.pixel,
                    (feature: FeatureLike, layer: Layer<Source, LayerRenderer<any>>) => {

                        //cluster feature that can contain several markers
                        const mayMultiMarkerFeature = feature.get('features') as Feature<Geometry>[]

                        if (mayMultiMarkerFeature.length === 1) { //single marker
                            const eventId = mayMultiMarkerFeature[0].get("eventId") as string
                            this.selectEvent(eventId)
                        } else if (mayMultiMarkerFeature.length > 1) { //marker group => various event markers
                            this.map.getView().animate({
                                center: mapClickEvent.coordinate,
                                zoom: this.map.getView().getZoom() ? this.map.getView().getZoom()! + 2 : this.initialZoom + 2,
                                duration: this.mapAnimationDuration
                            })
                        }
                    },
                    {
                        layerFilter: candidate => candidate === this.eventMarkersLayer,
                        hitTolerance: this.markerHitTolerance
                    }
                )
            }
        })
    }

    private toEventMarker(event: EventDetail): Feature<Point> {
        const olCoordinates = fromLonLat([event.location.longitude, event.location.latitude])
        return new Feature<Point>({
            geometry: new Point(olCoordinates),
            eventId: event.id,
        })
    }

    private buildMarkerStyle(iconPath: string): Style {
        return new Style({
            image: new Icon({
                src: iconPath,
                anchor: [0.5, 1],
                opacity: 1
            })
        })
    }

    private selectEvent(eventId: string | null) {
        //unselect previously selected trainer marker
        if (this.selectedEventId) {
            this.eventMarkersById[this.selectedEventId].set("selected", false)
        }
        //select new trainer
        if (eventId && this.eventMarkersById[eventId]) {
            this.eventMarkersById[eventId].set("selected", true)
        }
        this.selectedEventId = eventId
    }

    private displayEvents(events: EventDetail[]) {
        this.eventMarkersById = {}
        let eventMarkers: Feature<Point>[] = []
        events.forEach(event => {
            let eventMarker = this.toEventMarker(event)
            eventMarkers.push(eventMarker)
            this.eventMarkersById[event.id] = eventMarker
        })
        this.eventMarkersSource.clear()
        this.eventMarkersSource.addFeatures(eventMarkers)
    }

    private initEmptyMarkerLayer(): void {
        this.eventMarkersSource = new VectorSource({features: []})
        this.clusteredEventSource = new Cluster({
            distance: this.clusterMinDistance,
            source: this.eventMarkersSource
        })
        this.eventMarkersLayer = new VectorLayer({
            source: this.clusteredEventSource,
            style: (clusterFeature) => {
                const clusteredFeatures = clusterFeature.get('features') as Feature[]
                let markerImagePath
                if (clusteredFeatures.length > 1) { // Several trainers in the cluster, use group icon
                    markerImagePath = this.groupMarkerPath
                } else { //mono marker
                    let feature = clusteredFeatures[0]
                    if (feature.get("selected")) { //selected marker
                        markerImagePath = this.selectedMarkerPath
                    } else { //normal marker
                        markerImagePath = this.baseMarkerPath
                    }
                }
                return this.buildMarkerStyle(markerImagePath)
            }
        })
    }
}
