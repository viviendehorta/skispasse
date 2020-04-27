import { LocationCoordinate } from 'app/shared/model/location-coordinate.model';
import { NewsCategory } from 'app/shared/model/news-category.model';

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
