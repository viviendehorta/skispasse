import {ILocationCoordinate, LocationCoordinate} from './location-coordinate.model';
import {IMedia} from "./media.model";

export interface INewsFactDetail {
  address: string;
  city: string;
  country: string;
  createdDate: string;
  eventDate: string;
  id: string;
  locationCoordinate: ILocationCoordinate;
  newsCategoryId: string;
  newsCategoryLabel: string;
  media: IMedia;
}

export class NewsFactDetail implements INewsFactDetail {
  address: string;
  city: string;
  country: string;
  createdDate: string;
  eventDate: string;
  id: string;
  locationCoordinate: ILocationCoordinate;
  newsCategoryId: string;
  newsCategoryLabel: string;
  media: IMedia;

  constructor(
    address?: string,
    city?: string,
    country?: string,
    createdDate?: string,
    eventDate?: string,
    id?: string,
    locationCoordinate?: ILocationCoordinate,
    newsCategoryId?: string,
    newsCategoryLabel?: string,
    media?: IMedia
  ) {
    this.address = address ? address : null;
    this.city = city ? city : null;
    this.country = country ? country : null;
    this.createdDate = createdDate ? createdDate : null;
    this.eventDate = eventDate ? eventDate : null;
    this.id = id ? id : null;
    this.locationCoordinate = locationCoordinate ? locationCoordinate : new LocationCoordinate();
    this.newsCategoryId = newsCategoryId ? newsCategoryId : null;
    this.newsCategoryLabel = newsCategoryLabel ? newsCategoryLabel : null;
    this.media = media ? media : null;
  }
}
