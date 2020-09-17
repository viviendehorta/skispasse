import {Injectable} from '@angular/core';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {NewsFactMarker} from "./news-fact-marker";
import {fromLonLat} from "ol/proj";

@Injectable({providedIn: 'root'})
export class NewsFactMarkerService {

    toNewsFactMarker(newsFactNoDetail: NewsFactNoDetail): NewsFactMarker {
        const olCoordinates = fromLonLat([newsFactNoDetail.locationCoordinate.longitude, newsFactNoDetail.locationCoordinate.latitude]);
        return new NewsFactMarker(newsFactNoDetail, olCoordinates);
    }

    toNewsFactMarkers(newsFactNoDetails: NewsFactNoDetail[]): NewsFactMarker[] {
        return newsFactNoDetails.map(newsFact => {
            return this.toNewsFactMarker(newsFact);
        });
    }
}
