import { ILocationCoordinate, LocationCoordinate } from 'app/shared/model/location-coordinate.model';

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
  videoPath: string;
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
  videoPath: string;

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
    videoPath?: string
  ) {
    this.address = address ? address : null;
    this.city = city ? city : null;
    this.country = country ? country : null;
    this.createdDate = createdDate ? createdDate : null;
    this.eventDate = eventDate ? eventDate : null;
    this.id = id ? id : null;
    this.locationCoordinate = locationCoordinate ? locationCoordinate : new LocationCoordinate(locationCoordinate.x, locationCoordinate.y);
    this.newsCategoryId = newsCategoryId ? newsCategoryId : null;
    this.newsCategoryLabel = newsCategoryLabel ? newsCategoryLabel : null;
    this.videoPath = videoPath ? videoPath : null;
  }
}
