import {Injectable} from '@angular/core';
import Feature from 'ol/Feature';
import {Cluster, Vector as VectorSource} from 'ol/source';
import {Vector as VectorLayer} from 'ol/layer';
import View from 'ol/View';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {
    SMALL_MULTIPLE_NEWS_FACTS_STYLE,
    SMALL_IMG_STYLE_BY_NEWS_CATEGORY,
    SMALL_MARKER_ICON_SIZE_IN_PIXEL
} from "./marker-style.constants";
import {NewsFactMarkerService} from "./news-fact-marker.service";

@Injectable({providedIn: 'root'})
export class OpenLayersService {

    constructor(private newsFactMarkerService: NewsFactMarkerService) {
    }

    buildView(center: number[], zoom: number): View {
        return new View({
            center,
            zoom,
            constrainResolution: true
        });
    }

    buildNewsFactMarkerLayer(newsFacts: NewsFactNoDetail[]): VectorLayer {
        const newsFactMarkers = this.newsFactMarkerService.toNewsFactMarkers(newsFacts);

        const newsFactMarkerSource = new VectorSource({features: newsFactMarkers});

        const clusterSource = new Cluster({
            distance: SMALL_MARKER_ICON_SIZE_IN_PIXEL + 4, // Add 4 pixels to MARKER_ICON_SIZE_IN_PIXEL to never have icon collision
            source: newsFactMarkerSource
        });

        return new VectorLayer({
            source: clusterSource,
            style: (feature) => {
                const clusterFeatures = feature.get('features') as Feature[];
                if (clusterFeatures.length > 1) { // Several news facts in the cluster, use group icon
                    return SMALL_MULTIPLE_NEWS_FACTS_STYLE;
                }
                return SMALL_IMG_STYLE_BY_NEWS_CATEGORY[(clusterFeatures[0].get('newsCategoryId'))];
            }
        });
    }

    refreshNewsFactMarkerLayer(newsFacts: NewsFactNoDetail[], newsFactMarkerLayer: VectorLayer): void {
        const cluster = newsFactMarkerLayer.getSource() as Cluster;
        cluster.getSource().clear();
        cluster.getSource().addFeatures(this.newsFactMarkerService.toNewsFactMarkers(newsFacts));
    }
}
