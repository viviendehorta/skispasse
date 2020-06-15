import {Injectable} from '@angular/core';
import TileLayer from 'ol/layer/Tile';
import Feature from 'ol/Feature';
import {TileJSON, Vector as VectorSource, XYZ} from 'ol/source';
import {Tile, Vector as VectorLayer} from 'ol/layer';
import View from 'ol/View';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {MappingService} from '../mapping/mapping.service';
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";

@Injectable({providedIn: 'root'})
export class OpenLayersService {
    constructor(private mappingService: MappingService) {
    }

    buildView(center: number[], zoom: number): View {
        return new View({
            center,
            zoom,
            constrainResolution: true
        });
    }

    buildMarkerVectorLayer(allNewsFacts: NewsFactNoDetail[]): VectorLayer {
        const vectorSource = new VectorSource({
            features: this.mappingService.newsFactNoDetailsToFeatures(allNewsFacts)
        });
        return new VectorLayer({
            source: vectorSource
        });
    }

    refreshLayerFeatures(features: Feature[], vectorLayer: VectorLayer) {
        vectorLayer.getSource().clear();
        vectorLayer.getSource().addFeatures(features);
    }

    buildRasterLayer(jsonUrl: string): Tile {
        return new Tile({
          source: new TileJSON({
            url: jsonUrl,
            tileSize: 512,
            crossOrigin: 'anonymous'
          })
        });
    }
}
