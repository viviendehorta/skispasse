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
import {LocationInfoService} from "../../core/geocoding/location-info.service";

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
        private locationInfoService: LocationInfoService
    ) {
    }

    resolve(route: ActivatedRouteSnapshot) {
        const locationCoordinateString = route.params.locationCoordinateXY as string;

        if (!locationCoordinateString) {
            this.router.navigate(['/404']);
        }

        const locationCoordinateXYValues = locationCoordinateString.split(',');
        if (!locationCoordinateXYValues || locationCoordinateXYValues.length < 2) {
            this.router.navigate(['/404']);
        }

        const locationCoordinate = new LocationCoordinate(parseFloat(locationCoordinateXYValues[0]), parseFloat(locationCoordinateXYValues[1]));
        return this.locationInfoService.fetchLocationInfo(locationCoordinate).pipe(
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
        path: 'new/:locationCoordinateXY',
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
