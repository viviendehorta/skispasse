import { Injectable } from '@angular/core';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { NewsFactNoDetail } from 'app/shared/model/news-fact-no-detail.model';
import { Icon, Style } from 'ol/style';

@Injectable({ providedIn: 'root' })
export class MappingService {
  newsFactNoDetailsToFeatures(newsFactNoDetails: NewsFactNoDetail[]): Feature[] {
    return newsFactNoDetails.map(newsFact => {
      return this.newsFactNoDetailToFeature(newsFact);
    });
  }

  newsFactNoDetailToFeature(newsFactNoDetail: NewsFactNoDetail): Feature {
    const style = this.getMarkerStyle(newsFactNoDetail.newsCategoryId);
    const markerFeature = new Feature({
      geometry: new Point([newsFactNoDetail.locationCoordinate.x, newsFactNoDetail.locationCoordinate.y]),
      newsFactId: newsFactNoDetail.id
    });
    markerFeature.setStyle(style);
    return markerFeature;
  }

  private getMarkerStyle(categoryId: string): Style {
    return new Style({
      image: new Icon({
        anchor: [0.5, 1],
        src: 'content/images/news-categories/news_category_' + categoryId + '.svg'
      })
    });
  }
}
