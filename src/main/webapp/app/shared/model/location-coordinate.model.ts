export interface ILocationCoordinate {
  x: number;
  y: number;
}

export class LocationCoordinate implements ILocationCoordinate {
  x: number;
  y: number;

  constructor(x?: number, y?: number) {
    this.x = x ? x : null;
    this.y = y ? y : null;
  }
}
