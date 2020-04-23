import { LocationCoordinate } from 'app/shared/beans/location-coordinate.model';
import { NewsCategory } from 'app/shared/beans/news-category.model';

export interface NewsFactDetail {
  id: string;
  geoCoordinate: LocationCoordinate;
  eventDate: string;
  createdDate: string;
  category: NewsCategory;
  country: string;
  city: string;
  address: string;
  videoPath: string;
}
