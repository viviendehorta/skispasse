import {Injectable} from '@angular/core';
import Map from "ol/Map";
import olms from 'ol-mapbox-style';
import {from, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {flatMap} from "rxjs/operators";

@Injectable({providedIn: 'root'})
export class MapStyleService {

    constructor(private http: HttpClient) {
    }

    /**
     * Apply the Mapbox style json available at the given url to the given Map
     * @param styleJsonUrl the url of the Mapbox json style to apply
     * @param mapId the HTML id of the ol Map that will receive the Mapbox style
     * @return a promise resolving the resulting ol Map
     */
    applyRemoteMapboxStyle(styleJsonUrl: string, mapId: string): Observable<Map> {
        return from(olms(mapId, styleJsonUrl) as Promise<Map>);
    }

    /**
     * Apply the application Mapbox style (stored on server side))
     * @param mapId the HTML id of the ol Map that will receive the Mapbox style
     * @return a promise resolving the resulting ol Map
     */
    applyAppMapboxStyle(mapId: string): Observable<Map> {
        return this.fetchMapboxStyle().pipe(
            flatMap(mapboxStyle => {
                return from(olms(mapId, mapboxStyle) as Promise<Map>);
            })
        );
    }

    /**
     * Request server to get the application Mapbox style json
     */
    fetchMapboxStyle(): Observable<any> {
        return this.http.get('/maps/map-style');
    }
}
