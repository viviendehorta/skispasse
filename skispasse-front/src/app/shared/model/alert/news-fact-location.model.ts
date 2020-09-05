import {Address} from "../address.model";
import {LocationCoordinate} from "../location-coordinate.model";

export interface INewsFactLocation {
    address: Address,
    locationCoordinate: LocationCoordinate
}

export class NewsFactLocation implements INewsFactLocation {

    address: Address;
    locationCoordinate: LocationCoordinate;

    constructor(address: Address, locationCoordinate: LocationCoordinate) {
        this.address = address;
        this.locationCoordinate = locationCoordinate;
    }
}
