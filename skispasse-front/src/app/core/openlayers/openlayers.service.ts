import {Injectable} from '@angular/core';
import Feature from 'ol/Feature';
import {TileJSON, Vector as VectorSource} from 'ol/source';
import {Tile, Vector as VectorLayer} from 'ol/layer';
import View from 'ol/View';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {MappingService} from '../mapping/mapping.service';
import Map from "ol/Map";
import olms from 'ol-mapbox-style';

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

    /**
     * Build a raster (fixed-sized tiles, old-fashion) map layer using the given json url
     * @param jsonUrl
     */
    buildRasterMapLayer(jsonUrl: string): Tile {
        return new Tile({
            source: new TileJSON({
                url: jsonUrl,
                tileSize: 512,
                crossOrigin: 'anonymous'
            })
        });
    }

    /**
     * Apply the Mapbox json style available at the given url to the given Map
     * @param styleJsonUrl the url of the Mapbox json style to apply
     * @param mapId the HTML id of the ol Map that will receive the Mapbox style
     * @return a promise resolving the resulting ol Map
     */
    applyMapboxStyleToMap(styleJsonUrl: string, mapId: string): Promise<Map> {
        return olms(mapId, styleJsonUrl);
    }
}
