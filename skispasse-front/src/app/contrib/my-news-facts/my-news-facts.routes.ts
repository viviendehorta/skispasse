import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router, Routes} from '@angular/router';
import {MyNewsFactsComponent} from './my-news-facts.component';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {NewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {ResolvePaginationParamService} from '../../core/pagination/resolve-pagination-param-service';
import {NewsFactCreationComponent} from "./create-news-fact/news-fact-creation.component";
import {NewsFactEditionComponent} from "./update-news-fact/news-fact-edition.component";
import {LocationCoordinate} from "../../shared/model/location-coordinate.model";
import {INewsFactLocation, NewsFactLocation} from "../../shared/model/alert/news-fact-location.model";
import {map} from "rxjs/operators";
import {LocationInfo} from "../../shared/model/address.model";
import {GeocodingService} from "../../core/map/geocoding.service";

@Injectable({providedIn: 'root'})
export class NewsFactResolve implements Resolve<any> {
    constructor(private newsFactService: NewsFactService) {
    }

    resolve(route: ActivatedRouteSnapshot) {
        const id = route.params.id ? route.params.id : null;
        if (id) {
            return this.newsFactService.getNewsFactDetail(id);
        }
        return new NewsFactDetail();
    }
}

@Injectable({providedIn: 'root'})
export class NewsFactLocationResolve implements Resolve<INewsFactLocation> {
    constructor(
        private router: Router,
        private geocodingService: GeocodingService
    ) {
    }

    resolve(route: ActivatedRouteSnapshot) {
        const locationLatLongString = route.params.locationLatLong as string;

        if (!locationLatLongString) {
            this.router.navigate(['/404']);
        }

        const locationLatLongValues = locationLatLongString.split(',');
        if (!locationLatLongValues || locationLatLongValues.length < 2) {
            this.router.navigate(['/404']);
        }

        const locationCoordinate = new LocationCoordinate(parseFloat(locationLatLongValues[0]), parseFloat(locationLatLongValues[1]));
        return this.geocodingService.fetchLocationInfo(locationCoordinate).pipe(
            map((address: LocationInfo) => {
                return new NewsFactLocation(address, locationCoordinate)
            })
        );
    }
}

export const myNewsFactsRoutes: Routes = [
    {
        path: '',
        component: MyNewsFactsComponent,
        resolve: {
            pagingParams: ResolvePaginationParamService
        },
        data: {
            pageTitle: 'My publications',
            defaultSort: 'id,asc'
        }
    },
    {
        path: 'new/:locationLatLong',
        component: NewsFactCreationComponent,
        data: {
            pageTitle: 'New publication',
        },
        resolve: {
          newsFactLocation: NewsFactLocationResolve
        }
    },
    {
        path: ':id/edit',
        component: NewsFactEditionComponent,
        data: {
            pageTitle: 'Publication edition'
        },
        resolve: {
            newsFact: NewsFactResolve
        }
    }
];
