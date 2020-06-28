import {Injectable} from '@angular/core';
import {MEDIUM_IMG_STYLE_BY_NEWS_CATEGORY, MEDIUM_MULTIPLE_NEWS_FACTS_STYLE} from "./marker-style.constants";
import {Feature} from "ol";
import {NewsFactMarker} from "./news-fact-marker";
import {FeatureLike} from "ol/Feature";

@Injectable({providedIn: 'root'})
export class NewsFactMarkerSelectionService {

    private selectedMarker: Feature;

    selectMarker(newsFactClusterMarker: Feature) {
        this.resetMarkerSelection();
        this.selectedMarker = newsFactClusterMarker;

        const newsFactMarkers = newsFactClusterMarker.get('features') as NewsFactMarker[];

        if (newsFactMarkers.length > 1) { //Group marker
            this.selectedMarker.setStyle(MEDIUM_MULTIPLE_NEWS_FACTS_STYLE);
        } else if (newsFactMarkers.length === 1) { //Single marker
            const singleMarker = newsFactMarkers[0];
            this.selectedMarker.setStyle(MEDIUM_IMG_STYLE_BY_NEWS_CATEGORY[singleMarker.getNewsCategoryId()]);
        }
    }

    isSelectedMarker(marker: FeatureLike) {
        if (!this.selectedMarker) {
            return false;
        }

        const selectedNewsFactIds = (this.selectedMarker.get('features') as NewsFactMarker[]).map(marker => marker.getNewsFactId());
        const maySelectedNewsFactIds = (marker.get('features') as NewsFactMarker[]).map(marker => marker.getNewsFactId());

        //Count of features in the cluster is different => not equals
        if (selectedNewsFactIds.length !== maySelectedNewsFactIds.length) {
            return false;
        }

        //Compare ids of all news facts in the cluster to check equality
        return selectedNewsFactIds.filter(selectedNewsFactId => !maySelectedNewsFactIds.includes(selectedNewsFactId)).length === 0;
    }

    private resetMarkerSelection() {
        if (this.selectedMarker) {
            this.selectedMarker.setStyle(null);
        }
    }
}
