import { LocationCoordinate } from 'app/shared/beans/location-coordinate.model';
import { NewsCategory } from 'app/shared/beans/news-category.model';

export interface NewsFactDetail {
  id: string;
  locationCoordinate: LocationCoordinate;
  eventDate: string;
  createdDate: string;
  newsCategoryId: string;
  newsCategoryLabel: string;
  country: string;
  city: string;
  address: string;
  videoPath: string;
}
