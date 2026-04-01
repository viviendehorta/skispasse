import {CommonModule} from "@angular/common";
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {Feature, MapBrowserEvent, View} from "ol";
import {apply} from "ol-mapbox-style";
import {FeatureLike} from "ol/Feature";
import {Point} from "ol/geom";
import VectorLayer from "ol/layer/Vector";
import OLMap from "ol/Map";
import {fromLonLat, toLonLat} from "ol/proj";
import {Cluster} from "ol/source";
import VectorSource from "ol/source/Vector";
import {Icon, Style} from "ol/style";
import {from} from "rxjs";
import {environment} from "../../../environments/environment";
import {mapActions, mapFeature, MapState} from "../../core/map-store";
import {EventDetail} from "../../model/event-detail.model";
import {LonLat} from "../../model/lonlat.model";

@Component({
    templateUrl: "./event-map.component.html",
    selector: "sk-events-map",
    styleUrls: ["./event-map.component.scss"],
    imports: [
        CommonModule,
    ],
    standalone: true
})
export class EventMapComponent implements OnInit {
    readonly baseMarkerPath = "/assets/map-markers/unselected.png";
    readonly selectedMarkerPath = "/assets/map-markers/selected.png";
    readonly groupMarkerPath = "/assets/map-markers/group.png";

    readonly mapId = "eventsMap";
    readonly initialZoom = 1;
    readonly defaultMapCenterLonLat = [3.162845, 46.990896];
    //minimum distance allowed between markers before grouping into cluster
    readonly minDistanceInPixelsBetweenClusters = 32;
    readonly distanceInPixelAroundMarker = 3;
    readonly mapAnimationDuration = 1000;

    isSelectingLocation: boolean = false;
    isLoadingEvents: boolean = false;
    isAddingEvent: boolean = false;
    isImportingEvents: boolean = false;

    map: OLMap;
    eventMarkersById!: { [id: string]: Feature<Point> };
    selectedEvent: EventDetail | null = null;

    eventMarkersLayer: VectorLayer;
    eventMarkersSource: VectorSource;

    constructor(
        private mapStore: Store<MapState>,
        private router: Router,
        private route: ActivatedRoute,
    ) {
    }

    ngOnInit(): void {
        this.configureMapLayers();
        this.mapStore.select(mapFeature.selectIsAddingEvent).subscribe(isAddingEvent => this.isAddingEvent = isAddingEvent);
        this.mapStore.select(mapFeature.selectIsLoadingEvents).subscribe(isLoadingEvents => this.isLoadingEvents = isLoadingEvents);
        this.mapStore.select(mapFeature.selectIsImportingEvents).subscribe(isImporting => this.isImportingEvents = isImporting);
        this.mapStore.select(mapFeature.selectSelectingLocation).subscribe(selectResult => this.isSelectingLocation = selectResult);
        this.mapStore.select(mapFeature.selectEvents).subscribe(events => this.displayEventsOnMap(events));
        this.mapStore.select(mapFeature.selectSelectedEventDetail).subscribe(newSelectedEvent => {
            //unselect previously selected event
            if (this.selectedEvent && this.selectedEvent.id !== newSelectedEvent?.id) {
                this.eventMarkersById[this.selectedEvent.id].set("selected", false);
            }
            //select new event
            if (newSelectedEvent && this.eventMarkersById[newSelectedEvent.id]) {
                this.eventMarkersById[newSelectedEvent.id].set("selected", true);
                this.router.navigate([`/event-detail/${newSelectedEvent.id}`]);
            }
            this.selectedEvent = newSelectedEvent;
        });

        from(apply(new OLMap({
            view: new View({
                constrainResolution: true,
                center: fromLonLat(this.defaultMapCenterLonLat),
                zoom: this.initialZoom,
            })
        }), environment.mapStyleLink)).subscribe(styledMap => {
            let map = styledMap as OLMap;
            map.setTarget(this.mapId);
            map.addLayer(this.eventMarkersLayer);
            this.map = map;
            this.initMapInteractions();
        });

        this.mapStore.dispatch(mapActions.loadEvents());
    }

    private initMapInteractions() {
        this.map.on("click", (mapClickEvent: MapBrowserEvent) => {
            if (this.isSelectingLocation) {
                let olLonLat = toLonLat(mapClickEvent.coordinate);
                let location: LonLat = {
                    longitude: olLonLat[0],
                    latitude: olLonLat[1]
                };
                this.mapStore.dispatch(mapActions.setLocationForEventCreation({location: location}));
                this.router.navigate(["event-creation"]);
            } else {
                let markerFeaturesAtPixelClicked: FeatureLike[] = this.map.getFeaturesAtPixel(mapClickEvent.pixel, {
                    layerFilter: layer => layer === this.eventMarkersLayer,
                    hitTolerance: this.distanceInPixelAroundMarker
                });

                if (markerFeaturesAtPixelClicked.length) {
                    let groupFeatureCandidates = markerFeaturesAtPixelClicked[0].get('features') as FeatureLike[];
                    if (groupFeatureCandidates.length > 1) { //marker group => various event markers
                        this.map.getView().animate({
                            center: mapClickEvent.coordinate,
                            zoom: this.map.getView().getZoom() ? this.map.getView().getZoom()! + 2 : this.initialZoom + 2,
                            duration: this.mapAnimationDuration
                        });
                    } else if (groupFeatureCandidates.length === 1) { //single marker
                        const eventId = groupFeatureCandidates[0].get("eventId") as string;
                        this.selectEvent(eventId);
                    }
                } else { //clear selection
                    this.resetEventSelection();
                }
            }
        });
    }

    private toEventMarker(event: EventDetail): Feature<Point> {
        return new Feature<Point>({
            geometry: new Point(fromLonLat([event.location.longitude, event.location.latitude])),
            eventId: event.id,
        });
    }

    private buildMarkerStyle(iconPath: string): Style {
        return new Style({
            image: new Icon({
                src: iconPath,
                anchor: [0.5, 1],
                opacity: 1
            })
        });
    }

    private selectEvent(eventId: string | null) {
        this.mapStore.dispatch(mapActions.setSelectedEvent({eventId: eventId}));
    }

    private displayEventsOnMap(events: EventDetail[]) {
        this.eventMarkersById = {};
        let eventMarkers: Feature<Point>[] = [];
        events.forEach(event => {
            let eventMarker = this.toEventMarker(event);
            eventMarkers.push(eventMarker);
            this.eventMarkersById[event.id] = eventMarker;
        });
        this.eventMarkersSource.clear();
        this.eventMarkersSource.addFeatures(eventMarkers);
    }

    private configureMapLayers(): void {
        this.eventMarkersSource = new VectorSource({features: []});
        this.eventMarkersLayer = new VectorLayer({
            source: new Cluster({
                distance: this.minDistanceInPixelsBetweenClusters,
                source: this.eventMarkersSource
            }),
            style: (clusterFeature) => {
                const clusteredFeatures = clusterFeature.get('features') as Feature[];
                let markerImagePath;
                if (clusteredFeatures.length > 1) { // Several trainers in the cluster, use group icon
                    markerImagePath = this.groupMarkerPath;
                } else { //mono marker
                    let feature = clusteredFeatures[0];
                    if (feature.get("selected")) { //selected marker
                        markerImagePath = this.selectedMarkerPath;
                    } else { //normal marker
                        markerImagePath = this.baseMarkerPath;
                    }
                }
                return this.buildMarkerStyle(markerImagePath);
            }
        });
    }

    private resetEventSelection() {
        if (this.selectedEvent) {
            // if (this.isOtherRouteActive()) {
            //     console.log("regarde la route");
            // }
            this.mapStore.dispatch(mapActions.setSelectedEvent({eventId: null}));
        }
    }

    private isOtherRouteActive(): boolean {
        return this.route.snapshot.url.length > 0;
    }
}
