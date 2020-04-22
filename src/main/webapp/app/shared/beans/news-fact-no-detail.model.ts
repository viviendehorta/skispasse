import { LocationCoordinate } from 'app/shared/beans/location-coordinate.model';

export interface NewsFactNoDetail {
  id: string;
  newsCategoryId: string;
  locationCoordinate: LocationCoordinate;
}
