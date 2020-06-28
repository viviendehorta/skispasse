import {Injectable} from '@angular/core';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {NewsFactMarker} from "./news-fact-marker";

@Injectable({providedIn: 'root'})
export class NewsFactMarkerService {

    toNewsFactMarker(newsFactNoDetail: NewsFactNoDetail): NewsFactMarker {
        return new NewsFactMarker(newsFactNoDetail.locationCoordinate, newsFactNoDetail.id, newsFactNoDetail.newsCategoryId);
    }

    toNewsFactMarkers(newsFactNoDetails: NewsFactNoDetail[]): NewsFactMarker[] {
        return newsFactNoDetails.map(newsFact => {
            return this.toNewsFactMarker(newsFact);
        });
    }
}
