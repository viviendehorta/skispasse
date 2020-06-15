import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {View} from 'ol';
import {fromLonLat} from "ol/proj";
import {OpenLayersService} from "../../core/openlayers/openlayers.service";
import {NewsFactService} from "../../core/newsfacts/news-fact.service";
import {NewsFactNoDetail} from "../../shared/model/news-fact-no-detail.model";

@Component({
    selector: 'skis-worldmap',
    templateUrl: './worldmap.component.html',
    styleUrls: ['./worldmap.scss']
})
export class WorldmapComponent implements AfterViewInit, OnDestroy {

    MAP_ID = 'worldmapPageNewsFactsMap';
    newsFacts: NewsFactNoDetail[];
    newsFactMap: Map;

    constructor(
        private newsFactService: NewsFactService,
        private openLayersService: OpenLayersService
    ) {}

    ngAfterViewInit() {
      this.newsFactService.getAll().subscribe((newsFacts) => {
        this.newsFacts = newsFacts;
        this.buildNewsFactsMap()
      })
    }

    ngOnDestroy() {
    }

    buildNewsFactsMap() {
        this.newsFactMap = new Map({
            layers: [
                this.openLayersService.buildRasterLayer('https://api.maptiler.com/maps/basic/tiles.json?key=vRGImi4p2hajjVUCsIAE'),
                this.openLayersService.buildMarkerVectorLayer(this.newsFacts)
            ],
            target: this.MAP_ID,
            view: new View({
                constrainResolution: true,
                center: fromLonLat([0, 0]),
                zoom: 2
            })
        });
    }
}
