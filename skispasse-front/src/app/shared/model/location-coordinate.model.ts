export interface ILocationCoordinate {
  latitude: number;
  longitude: number;
}

export class LocationCoordinate implements ILocationCoordinate {
  latitude: number;
  longitude: number;

  constructor(latitude?: number, longitude?: number) {
    this.latitude = latitude ? latitude : null;
    this.longitude = longitude ? longitude : null;
  }
}
