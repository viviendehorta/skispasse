import {Observable} from "rxjs";
import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {map} from "rxjs/operators";
import {Address, IAddress} from "../../shared/model/address.model";
import {GeocodeAddress} from "./geocode-address.model";
import {LocationCoordinate} from "../../shared/model/location-coordinate.model";
import {toLonLat} from 'ol/proj';

@Injectable({providedIn: 'root'})
export class AddressFinderService {
    reverseGeocodeApiUrl = 'https://api.bigdatacloud.net/data/reverse-geocode-client';

    constructor(private http: HttpClient) {
    }

    findAddress(locationCoordinate: LocationCoordinate): Observable<IAddress> {
        const lonLat = toLonLat([locationCoordinate.x, locationCoordinate.y]);
        return this.http.get<GeocodeAddress>(this.reverseGeocodeApiUrl, {
                params: {
                    latitude: lonLat[1].toString(),
                    longitude: lonLat[0].toString(),
                    localityLanguage: 'en'
                },
                observe: 'response'
            })
            .pipe(
                map((httpResponse: HttpResponse<GeocodeAddress>) => {
                    const geocodeAddress = httpResponse.body;
                    return new Address(
                        geocodeAddress.countryName,
                        geocodeAddress.city,
                        geocodeAddress.locality
                    );
                })
            );
    }
}
