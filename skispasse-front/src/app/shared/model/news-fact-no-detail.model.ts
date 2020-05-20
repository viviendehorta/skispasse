import {ILocationCoordinate} from './location-coordinate.model';

export interface NewsFactNoDetail {
  id: string;
  newsCategoryId: string;
  locationCoordinate: ILocationCoordinate;
}
