import React from 'react';
import {Switch} from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Logout from 'app/modules/login/logout';
import WorldMapPage from "app/modules/worldmap/worldmap";
import PrivateRoute from 'app/components/authentication/private-route';
import ErrorBoundaryRoute from 'app/components/error/error-boundary-route';
import PageNotFound from 'app/components/error/page-not-found';
import {AUTHORITIES} from 'app/config/constants';

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => <div>loading ...</div>
});

const Routes = () => (
  <div className="view-routes">
    <Switch>
      <ErrorBoundaryRoute path="/login" component={Login} />
      <ErrorBoundaryRoute path="/logout" component={Logout} />
      <PrivateRoute path="/admin" component={Admin} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute path="/" exact component={WorldMapPage} />
      <ErrorBoundaryRoute component={PageNotFound} />
    </Switch>
  </div>
);

export default Routes;
