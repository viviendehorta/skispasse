import {Injectable, NgModule} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterModule} from '@angular/router';
import {errorRoute} from './shared/error/error.route';

import {UserRouteAccessService} from './core/auth/user-route-access-service';
import {WorldmapComponent} from './public/worldmap/worldmap.component';
import {ROLE_ADMIN, ROLE_CONTRIBUTOR, ROLE_USER} from './shared/constants/role.constants';
import {environment} from '../environments/environment';
import {NewsFactDetailModalContentComponent} from "./public/worldmap/newsfactmodal/news-fact-detail/news-fact-detail-modal.content.component";
import {NewsFactService} from "./core/newsfacts/news-fact.service";
import {NewsFactDetail} from "./shared/model/news-fact-detail.model";

const LAYOUT_ROUTES = [...errorRoute];

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

@NgModule({
    imports: [
        RouterModule.forRoot([
                {
                    path: 'newsfact/:id',
                    component: NewsFactDetailModalContentComponent,
                    outlet: 'modal',
                    resolve: {
                        newsFact: NewsFactResolve
                    }
                },
                {
                    path: 'map',
                    component: WorldmapComponent,
                    data: {
                        authorities: [],
                        pageTitle: 'World Map'
                    }
                },
                {
                    path: 'admin',
                    data: {
                        authorities: [ROLE_ADMIN]
                    },
                    canActivate: [UserRouteAccessService],
                    loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule)
                },
                {
                    path: 'contrib',
                    data: {
                        authorities: [ROLE_CONTRIBUTOR]
                    },
                    canActivate: [UserRouteAccessService],
                    loadChildren: () => import('./contrib/contrib-routing.module').then(m => m.ContribRoutingModule)
                },
                {
                    path: 'public',
                    loadChildren: () => import('./public/public-routing.module').then(m => m.PublicRoutingModule)
                },
                {
                    path: 'user',
                    data: {
                        authorities: [ROLE_USER]
                    },
                    canActivate: [UserRouteAccessService],
                    loadChildren: () => import('./user/user-routing.module').then(m => m.UserRoutingModule)
                },
                ...LAYOUT_ROUTES
            ],
            {enableTracing: environment.debugInfoEnabled}
        )
    ],
    exports: [RouterModule]
})
export class SkispasseAppRoutingModule {
}
