import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router, Routes} from '@angular/router';
import {MyNewsFactsComponent} from './my-news-facts.component';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {NewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {ResolvePaginationParamService} from '../../core/pagination/resolve-pagination-param-service';
import {NewsFactCreationComponent} from "./create-news-fact/news-fact-creation.component";
import {NewsFactEditionComponent} from "./update-news-fact/news-fact-edition.component";
import {NewsFactNoDetail} from "../../shared/model/news-fact-no-detail.model";
import {ILocationCoordinate, LocationCoordinate} from "../../shared/model/location-coordinate.model";

@Injectable({ providedIn: 'root' })
export class NewsFactResolve implements Resolve<any> {
  constructor(private newsFactService: NewsFactService) {}

  resolve(route: ActivatedRouteSnapshot) {
    const id = route.params.id ? route.params.id : null;
    if (id) {
      return this.newsFactService.getNewsFactDetail(id);
    }
    return new NewsFactDetail();
  }
}

@Injectable({ providedIn: 'root' })
export class LocationCoordinateResolve implements Resolve<ILocationCoordinate> {
  constructor(private router:Router) {}

  resolve(route: ActivatedRouteSnapshot) {
    const locationCoordinateString = route.params.locationCoordinateXY as string;

    if (!locationCoordinateString) {
      this.router.navigate(['/404']);
    }

    const locationCoordinateXYValues = locationCoordinateString.split(',');
    if (!locationCoordinateXYValues || locationCoordinateXYValues.length < 2) {
      this.router.navigate(['/404']);
    }

    const locationCoordinateX = parseFloat(locationCoordinateXYValues[0]);
    const locationCoordinateY = parseFloat(locationCoordinateXYValues[1]);
    return new LocationCoordinate(locationCoordinateX, locationCoordinateY);
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
    resolve : {
      locationCoordinate: LocationCoordinateResolve
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
