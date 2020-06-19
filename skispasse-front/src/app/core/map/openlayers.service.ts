import {Injectable} from '@angular/core';
import Feature from 'ol/Feature';
import {Vector as VectorSource} from 'ol/source';
import {Vector as VectorLayer} from 'ol/layer';
import View from 'ol/View';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import Point from "ol/geom/Point";
import {Icon, Style} from "ol/style";

@Injectable({providedIn: 'root'})
export class OpenLayersService {

    private PNG_ICON_NAME_BY_NEWS_CATEGORY = {
        '1': 'red-32', //Manifestation
        '2': 'blue-32', //Sport
        '3': 'pink-32', //Culture
        '4': 'yellow-32', //Spectacle
        '5': 'green-32', //Nature
        '6': 'grey-32' //Autre
    };


    buildView(center: number[], zoom: number): View {
        return new View({
            center,
            zoom,
            constrainResolution: true
        });
    }

    buildMarkerVectorLayer(allNewsFacts: NewsFactNoDetail[]): VectorLayer {
        const vectorSource = new VectorSource({
            features: this.toFeatures(allNewsFacts)
        });
        return new VectorLayer({
            source: vectorSource
        });
    }

    refreshNewsFactMarkerLayer(newsFacts: NewsFactNoDetail[], newsFactMarkerLayer: VectorLayer) {
        newsFactMarkerLayer.getSource().clear();
        newsFactMarkerLayer.getSource().addFeatures(this.toFeatures(newsFacts));
    }

    private toFeature(newsFactNoDetail: NewsFactNoDetail): Feature {
        const style = this.getMarkerStyle(newsFactNoDetail.newsCategoryId);
        const markerFeature = new Feature({
            geometry: new Point([newsFactNoDetail.locationCoordinate.x, newsFactNoDetail.locationCoordinate.y]),
            newsFactId: newsFactNoDetail.id
        });
        markerFeature.setStyle(style);
        return markerFeature;
    }

    private toFeatures(newsFactNoDetails: NewsFactNoDetail[]): Feature[] {
        return newsFactNoDetails.map(newsFact => {
            return this.toFeature(newsFact);
        });
    }

    private getMarkerStyle(categoryId: string): Style {
        return new Style({
            image: new Icon({
                src: `/assets/images/markers/32/${this.PNG_ICON_NAME_BY_NEWS_CATEGORY[categoryId]}.png`,
                anchor: [0.5, 1],
                opacity: 1
            })
        });
    }
}
