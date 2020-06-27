import {Injectable} from '@angular/core';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {NewsFactMarker} from "./news-fact-marker";
import {MEDIUM_IMG_STYLE_BY_NEWS_CATEGORY, MEDIUM_MULTIPLE_NEWS_FACTS_STYLE} from "./marker-style.constants";
import {Feature} from "ol";

@Injectable({providedIn: 'root'})
export class NewsFactMarkerService {

    private selectedMarker: Feature;

    toNewsFactMarker(newsFactNoDetail: NewsFactNoDetail): NewsFactMarker {
        return new NewsFactMarker(newsFactNoDetail.locationCoordinate, newsFactNoDetail.id, newsFactNoDetail.newsCategoryId);
    }

    toNewsFactMarkers(newsFactNoDetails: NewsFactNoDetail[]): NewsFactMarker[] {
        return newsFactNoDetails.map(newsFact => {
            return this.toNewsFactMarker(newsFact);
        });
    }

    selectMarker(newsFactClusterMarker: Feature, isGroupMarker?: boolean) {
        this.resetMarkerSelection();
        this.selectedMarker = newsFactClusterMarker;

        if (isGroupMarker) {
            this.selectedMarker.setStyle(MEDIUM_MULTIPLE_NEWS_FACTS_STYLE);
        } else {
            const singleMarker = newsFactClusterMarker.get('features')[0] as NewsFactMarker;
            this.selectedMarker.setStyle(MEDIUM_IMG_STYLE_BY_NEWS_CATEGORY[singleMarker.getNewsCategoryId()]);
        }
    }

    private resetMarkerSelection() {
        if (this.selectedMarker) {
            this.selectedMarker.setStyle(null);
        }
    }
}
