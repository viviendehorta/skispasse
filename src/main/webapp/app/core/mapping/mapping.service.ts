import { Injectable } from '@angular/core';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import Style from 'ol/style/Style';
import Icon from 'ol/style/Icon';

@Injectable({ providedIn: 'root' })
export class MappingService {
  newsFactNoDetailsToFeatures(noDetailNewsFacts: any[]): Feature[] {
    return noDetailNewsFacts.map(newsFact => {
      return this.newsFactNoDetailToFeature(newsFact);
    });
  }

  newsFactNoDetailToFeature(noDetailNewsFact: any): Feature {
    const style = this.getMarkerStyle(noDetailNewsFact.categoryId);
    const markerFeature = new Feature({
      geometry: new Point([noDetailNewsFact.locationCoordinate.x, noDetailNewsFact.locationCoordinate.y]),
      newsFactId: noDetailNewsFact.id
    });
    markerFeature.setStyle(style);
    return markerFeature;
  }

  getMarkerStyle(categoryId: number) {
    return new Style({
      image: new Icon({
        anchor: [0.5, 1],
        src: 'content/images/news-categories/news-category' + categoryId + '.svg'
      })
    });
  }
}
