import {Observable} from "rxjs";
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from "rxjs/operators";
import {ILocationInfo, LocationInfo} from "../../shared/model/address.model";
import {LocationCoordinate} from "../../shared/model/location-coordinate.model";
import {toLonLat} from 'ol/proj';
import {environment} from "../../../environments/environment";

@Injectable({providedIn: 'root'})
export class GeocodingService {
    private resourceUrl = environment.serverUrl + 'maps/location-info/';

    constructor(private http: HttpClient) {
    }

    fetchLocationInfo(locationCoordinate: LocationCoordinate): Observable<ILocationInfo> {
        return this.http.get<ILocationInfo>(this.resourceUrl, {
            params: {
                longitude: locationCoordinate.longitude.toString(),
                latitude: locationCoordinate.latitude.toString()
            },
            observe: 'response'
        }).pipe(
            map((httpResponse) => {
                return new LocationInfo(
                    httpResponse.body.country,
                    httpResponse.body.city,
                    httpResponse.body.locality
                );
            })
        );
    }
}
