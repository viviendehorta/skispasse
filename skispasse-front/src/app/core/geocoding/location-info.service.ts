import {Observable, of} from "rxjs";
import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {catchError, map} from "rxjs/operators";
import {ILocationInfo, LocationInfo} from "../../shared/model/address.model";
import {LocationCoordinate} from "../../shared/model/location-coordinate.model";
import {toLonLat} from 'ol/proj';
import {environment} from "../../../environments/environment";

@Injectable({providedIn: 'root'})
export class LocationInfoService {

    private EMPTY_LOCATION_INFO = new LocationInfo('', '', '');

    constructor(private http: HttpClient) {
    }

    fetchLocationInfo(locationCoordinate: LocationCoordinate): Observable<ILocationInfo> {
        const lonLat = toLonLat([locationCoordinate.x, locationCoordinate.y]);
        return this.http.get<any>(environment.reverseGeoCodeAPIUrl, {
            params: {
                latitude: lonLat[1].toString(),
                longitude: lonLat[0].toString(),
                localityLanguage: 'en'
            },
            observe: 'response'
        }).pipe(
            map((httpResponse: HttpResponse<any>) => {
                console.log(JSON.stringify(httpResponse.body.localityInfo));
                const responseBody = httpResponse.body;
                return new LocationInfo(
                    responseBody.countryName,
                    responseBody.city,
                );
            }),
            catchError((error) => {
                return of(this.EMPTY_LOCATION_INFO);
            })
        );
    }
}
