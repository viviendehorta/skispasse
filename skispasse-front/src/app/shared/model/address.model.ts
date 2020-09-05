export interface IAddress {
    country: string,
    city: string,
    detail: string
}

export class Address implements IAddress {

    country: string;
    city: string;
    detail: string;

    constructor(country: string, city: string, detail?: string) {
        this.country = country;
        this.city = city;
        this.detail = detail ? detail : null;
    }
}
