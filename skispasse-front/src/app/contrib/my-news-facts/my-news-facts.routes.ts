import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Routes} from '@angular/router';
import {MyNewsFactsComponent} from './my-news-facts.component';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {NewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {ResolvePaginationParamService} from '../../core/pagination/resolve-pagination-param-service';
import {NewsFactCreationComponent} from "./create-news-fact/news-fact-creation.component";
import {NewsFactEditionComponent} from "./update-news-fact/news-fact-edition.component";

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
    path: 'new',
    component: NewsFactCreationComponent,
    data: {
      pageTitle: 'New publication',
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
