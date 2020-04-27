import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';

import { User } from 'app/shared/model//user.model';
import { PublishedNewsFactsComponent } from './published-news-facts.component';
import { PublishedNewsFactUpdateComponent } from './published-news-fact-update.component';
import { NewsFactService } from 'app/core/newsfacts/news-fact.service';

@Injectable({ providedIn: 'root' })
export class PublishedNewsFactsResolve implements Resolve<any> {
  constructor(private newsFactService: NewsFactService) {}

  resolve(route: ActivatedRouteSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.newsFactService.getNewsFactDetail(id);
    }
    return new User();
  }
}

export const publishedNewsFactsRoutes: Routes = [
  {
    path: '',
    component: PublishedNewsFactsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'publishedNewsFacts.list.title',
      defaultSort: 'id,asc'
    }
  },
  {
    path: 'new',
    component: PublishedNewsFactUpdateComponent,
    data: {
      pageTitle: 'publishedNewsFacts.creation.title'
    },
    resolve: {
      user: PublishedNewsFactsResolve
    }
  },
  {
    path: ':id/edit',
    component: PublishedNewsFactUpdateComponent,
    data: {
      pageTitle: 'publishedNewsFacts.edition.title'
    },
    resolve: {
      user: PublishedNewsFactsResolve
    }
  }
];
