import { ILocationCoordinate } from 'app/shared/model/location-coordinate.model';

export interface NewsFactNoDetail {
  id: string;
  newsCategoryId: string;
  locationCoordinate: ILocationCoordinate;
}