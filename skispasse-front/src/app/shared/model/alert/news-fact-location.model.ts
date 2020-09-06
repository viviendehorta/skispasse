import {LocationInfo} from "../address.model";
import {LocationCoordinate} from "../location-coordinate.model";

export interface INewsFactLocation {
    address: LocationInfo,
    locationCoordinate: LocationCoordinate
}

export class NewsFactLocation implements INewsFactLocation {
    address: LocationInfo;
    locationCoordinate: LocationCoordinate;

    constructor(address: LocationInfo, locationCoordinate: LocationCoordinate) {
        this.address = address;
        this.locationCoordinate = locationCoordinate;
    }
}
