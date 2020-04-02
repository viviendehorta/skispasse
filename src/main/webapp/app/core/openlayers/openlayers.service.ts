import { Injectable } from '@angular/core';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import Feature from 'ol/Feature';
import { Vector as VectorSource } from 'ol/source';
import { Vector as VectorLayer } from 'ol/layer';
import View from 'ol/View';
import { MappingService } from 'app/core/mapping/mapping.service';
import { NewsFactNoDetail } from 'app/shared/beans/news-fact-no-detail.model';

@Injectable({ providedIn: 'root' })
export class OpenLayersService {
  constructor(private mappingService: MappingService) {}

  buildView(center: number[], zoom: number): View {
    return new View({
      center,
      zoom
    });
  }

  buildOSMTileLayer() {
    return new TileLayer({
      source: new OSM()
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
}
