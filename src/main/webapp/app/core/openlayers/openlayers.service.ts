import { Injectable } from '@angular/core';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import Feature from 'ol/Feature';
import { Vector as VectorSource } from 'ol/source';
import { Icon, Style } from 'ol/style';
import { Vector as VectorLayer } from 'ol/layer';
import View from 'ol/View';
import { MappingService } from 'app/core/mapping/mapping.service';

@Injectable({ providedIn: 'root' })
export class OpenLayersService {
  constructor(private mappingService: MappingService) {}

  buildView(center: number[], zoom: number): View {
    return new View({
      center,
      zoom
    });
  }

  getMarkerStyle(categoryId: number): Style {
    return new Style({
      image: new Icon({
        anchor: [0.5, 1],
        src: 'content/images/news-categories/news-category' + categoryId + '.svg'
      })
    });
  }

  buildOSMTileLayer() {
    return new TileLayer({
      source: new OSM()
    });
  }

  buildMarkerVectorLayer(allNewsFacts: any[]): VectorLayer {
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
