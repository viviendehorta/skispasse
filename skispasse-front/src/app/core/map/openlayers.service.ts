import {Injectable} from '@angular/core';
import Feature from 'ol/Feature';
import {Cluster, Vector as VectorSource} from 'ol/source';
import {Vector as VectorLayer} from 'ol/layer';
import View from 'ol/View';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import Point from "ol/geom/Point";
import {
    MULTIPLE_NEWS_FACTS_STYLE,
    SMALL_IMG_STYLE_BY_NEWS_CATEGORY,
    SMALL_MARKER_ICON_SIZE_IN_PIXEL
} from "./marker-style.constants";

@Injectable({providedIn: 'root'})
export class OpenLayersService {

    buildView(center: number[], zoom: number): View {
        return new View({
            center,
            zoom,
            constrainResolution: true
        });
    }

    buildNewsFactMarkerLayer(newsFacts: NewsFactNoDetail[]): VectorLayer {
        const features = this.toFeatures(newsFacts);

        const featureSource = new VectorSource({features});

        const clusterSource = new Cluster({
            distance: SMALL_MARKER_ICON_SIZE_IN_PIXEL + 4, // Add 4 pixels to MARKER_ICON_SIZE_IN_PIXEL to never have icon collision
            source: featureSource
        });

        return new VectorLayer({
            source: clusterSource,
            style: (feature) => {
                const clusterFeatures = feature.get('features') as Feature[];
                if (clusterFeatures.length > 1) { // Several news facts in the cluster, use group icon
                    return MULTIPLE_NEWS_FACTS_STYLE;
                }
                return SMALL_IMG_STYLE_BY_NEWS_CATEGORY[(clusterFeatures[0].get('newsCategoryId'))];
            }
        });
    }

    refreshNewsFactMarkerLayer(newsFacts: NewsFactNoDetail[], newsFactMarkerLayer: VectorLayer): void {
        const cluster = newsFactMarkerLayer.getSource() as Cluster;
        cluster.getSource().clear();
        cluster.getSource().addFeatures(this.toFeatures(newsFacts));
    }

    private toFeature(newsFactNoDetail: NewsFactNoDetail): Feature {
        return new Feature({
            geometry: new Point([newsFactNoDetail.locationCoordinate.x, newsFactNoDetail.locationCoordinate.y]),
            newsFactId: newsFactNoDetail.id,
            newsCategoryId: newsFactNoDetail.newsCategoryId
        });
    }

    private toFeatures(newsFactNoDetails: NewsFactNoDetail[]): Feature[] {
        return newsFactNoDetails.map(newsFact => {
            return this.toFeature(newsFact);
        });
    }
}
