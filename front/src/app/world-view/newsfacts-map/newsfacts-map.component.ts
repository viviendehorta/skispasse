import {Component, Input, OnInit} from '@angular/core'
import {NewsFact} from "../../model/newsfact.model"
import OLMap from "ol/Map"
import {Feature, View} from "ol"
import {fromLonLat} from "ol/proj"
import {apply} from "ol-mapbox-style"
import {environment} from "../../../environments/environment"
import VectorLayer from "ol/layer/Vector"
import VectorSource from "ol/source/Vector"
import {Geometry, Point} from "ol/geom"
import {Cluster} from "ol/source"
import {Icon, Style} from "ol/style"

@Component({
    templateUrl: "./newsfacts-map.component.html",
    selector: "s-newsfacts-map"
})
export class NewsfactsMapComponent implements OnInit {

    readonly htmlId = "newsFactsMap"
    readonly initialZoom = 1
    readonly defaultMapCenterLonLat = [3.162845, 46.990896]

    //min distance between markers in pixels, when distance is smaller, then markers are grouped into the same cluster
    readonly clusterMinDistance = 32

    readonly baseMarkerPath = "/assets/map-markers/unselected.png"
    readonly selectedMarkerPath = "/assets/map-markers/selected.png"
    readonly groupMarkerPath = "/assets/map-markers/group.png"


    map!: OLMap
    isReadyMap: boolean = false //flag used to hide map during initialization
    newsfactMarkersLayer!: VectorLayer<VectorSource<Geometry>>

    @Input() newsFacts!: NewsFact[]
    @Input() selectedNewsFactIds: string[] = []

    constructor() {
    }

    ngOnInit(): void {
        let noStyleMap = new OLMap({
            view: new View({
                constrainResolution: true,
                center: fromLonLat(this.defaultMapCenterLonLat),
                zoom: this.initialZoom,
            })
        })

        apply(noStyleMap, environment.mapStyleLink).then(mapOrLayerGroup => {

            let styledMap = mapOrLayerGroup as OLMap

            this.initNewsfactMarkers(this.newsFacts, styledMap)

            styledMap.setTarget(this.htmlId) //display the map
            this.map = styledMap
            this.isReadyMap = true
        })
    }

    private initNewsfactMarkers(newsFacts: NewsFact[], map: OLMap): void {
        const newsFactMarkers = this.toNewsFactMarkers(newsFacts)

        const clusterSource = new Cluster({
            distance: this.clusterMinDistance,
            source: new VectorSource({features: newsFactMarkers})
        })

        this.newsfactMarkersLayer = new VectorLayer({
            source: clusterSource,
            style: (clusterFeature) => {

                const clusteredFeatures = clusterFeature.get('features') as Feature[]
                let markerImagePath

                if (clusteredFeatures.length > 1) { // several newsfacts in the cluster, use group icon
                    markerImagePath = this.groupMarkerPath

                } else { //single marker

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

        map.addLayer(this.newsfactMarkersLayer)
    }

    private toNewsFactMarker(newsFact: NewsFact): Feature {
        const olCoordinates = fromLonLat([newsFact.location.longitude, newsFact.location.latitude])
        return new Feature({
            geometry: new Point(olCoordinates),
            newsFact: newsFact,
        })
    }

    private toNewsFactMarkers(newsfacts: NewsFact[]): Feature[] {
        return newsfacts.map(newsFact => {
            return this.toNewsFactMarker(newsFact)
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
}
