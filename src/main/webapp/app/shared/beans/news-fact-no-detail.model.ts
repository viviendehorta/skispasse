import { LocationCoordinate } from 'app/shared/beans/location-coordinate.model';

export interface NewsFactNoDetail {
  id: number;
  categoryId: number;
  locationCoordinate: LocationCoordinate;
}
