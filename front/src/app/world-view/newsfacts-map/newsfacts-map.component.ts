import {Component, Input, OnInit} from '@angular/core'
import {NewsFact} from "../../model/newsfact.model"
import OLMap from "ol/Map"
import {View} from "ol"
import {fromLonLat} from "ol/proj"
import {apply} from "ol-mapbox-style"
import {environment} from "../../../environments/environment"

@Component({
    templateUrl: "./newsfacts-map.component.html",
    selector: "s-newsfacts-map"
})
export class NewsfactsMapComponent implements OnInit {

    //constants
    readonly htmlId = "newsFactsMap"
    readonly initialZoom = 1
    readonly defaultMapCenterLonLat = [3.162845, 46.990896]

    map!: OLMap

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
            styledMap.setTarget(this.htmlId)
            this.map = styledMap
        })
    }
}
