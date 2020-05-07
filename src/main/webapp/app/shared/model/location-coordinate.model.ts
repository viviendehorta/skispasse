export interface ILocationCoordinate {
  x: number;
  y: number;
}

export class LocationCoordinate implements ILocationCoordinate {
  x: number;
  y: number;

  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
  }
}
