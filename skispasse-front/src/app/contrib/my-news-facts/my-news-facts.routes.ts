import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Routes } from '@angular/router';
import { MyNewsFactsComponent } from './my-news-facts.component';
import { MyNewsFactEditionComponent } from './my-news-fact-edition.component';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {NewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {ResolvePaginationParamService} from '../../core/pagination/resolve-pagination-param-service';

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
      pageTitle: 'myNewsFacts.list.title',
      defaultSort: 'id,asc'
    }
  },
  {
    path: 'new',
    component: MyNewsFactEditionComponent,
    data: {
      pageTitle: 'myNewsFacts.creation.title'
    },
    resolve: {
      newsFact: NewsFactResolve
    }
  },
  {
    path: ':id/edit',
    component: MyNewsFactEditionComponent,
    data: {
      pageTitle: 'myNewsFacts.edition.title'
    },
    resolve: {
      newsFact: NewsFactResolve
    }
  }
];
