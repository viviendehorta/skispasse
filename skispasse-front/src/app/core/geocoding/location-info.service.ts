import {Observable, of} from "rxjs";
import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {catchError, map} from "rxjs/operators";
import {ILocationInfo, LocationInfo} from "../../shared/model/address.model";
import {LocationCoordinate} from "../../shared/model/location-coordinate.model";
import {toLonLat} from 'ol/proj';
import {BigDataCloudLocationInfo} from "./big-data-cloud-location-info.model";
import {environment} from "../../../environments/environment";

@Injectable({providedIn: 'root'})
export class LocationInfoService {

    private EMPTY_LOCATION_INFO = new LocationInfo('', '', '');

    constructor(private http: HttpClient) {
    }

    fetchLocationInfo(locationCoordinate: LocationCoordinate): Observable<ILocationInfo> {
        const lonLat = toLonLat([locationCoordinate.x, locationCoordinate.y]);
        return this.http.get<BigDataCloudLocationInfo>(environment.reverseGeoCodeAPIUrl, {
            params: {
                latitude: lonLat[1].toString(),
                longitude: lonLat[0].toString(),
                localityLanguage: 'en'
            },
            observe: 'response'
        }).pipe(
            map((httpResponse: HttpResponse<BigDataCloudLocationInfo>) => {
                const bigDataCloudLocationInfo = httpResponse.body;
                return new LocationInfo(
                    bigDataCloudLocationInfo.countryName,
                    bigDataCloudLocationInfo.city,
                    bigDataCloudLocationInfo.locality
                );
            }),
            catchError((error) => {
                return of(this.EMPTY_LOCATION_INFO);
            })
        );
    }
}
