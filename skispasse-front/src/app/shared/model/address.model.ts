export interface ILocationInfo {
    country: string,
    city: string,
    locality: string
}

export class LocationInfo implements ILocationInfo {

    country: string;
    city: string;
    locality: string;

    constructor(country: string, city: string, locality: string) {
        this.country = country;
        this.city = city;
        this.locality = locality;
    }
}
