import 'react-toastify/dist/ReactToastify.css';
import 'ol/ol.css';
import './app.scss';

import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {Card} from 'reactstrap';
import {BrowserRouter as Router} from 'react-router-dom';
import {toast, ToastContainer} from 'react-toastify';
import {hot} from 'react-hot-loader';

import {IRootState} from 'app/config/root.reducer';
import {getSession} from 'app/components/authentication/authentication.reducer';
import {getProfile} from 'app/environment/application-profile.reducer';
import {setLocale} from 'app/environment/locale.reducer';
import {hasAnyAuthority} from 'app/components/authentication/private-route';
import ErrorBoundary from 'app/components/error/error-boundary';
import {AUTHORITIES} from 'app/config/constants';
import AppRoutes from 'app/routes';
import {Brand} from "app/components/brand";

const baseHref = document
  .querySelector('base')
  .getAttribute('href')
  .replace(/\/$/, '');

export interface IAppProps extends StateProps, DispatchProps {
}

export const App = (props: IAppProps) => {
  useEffect(() => {
    props.getSession();
    props.getProfile();
  }, []);

  return (
    <Router basename={baseHref}>
      <div className="app-container">
        <ToastContainer position={toast.POSITION.TOP_LEFT} className="toastify-container"
                        toastClassName="toastify-toast"/>
        <div className="container-fluid view-container" id="app-view-container">
          <Card className="jh-card">

            <Brand/>

            <ErrorBoundary>
              <AppRoutes/>
            </ErrorBoundary>

          </Card>
        </div>
      </div>
    </Router>
  );
};

const mapStateToProps = ({authentication, applicationProfile, locale}: IRootState) => ({
  currentLocale: locale.currentLocale,
  isAuthenticated: authentication.isAuthenticated,
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN]),
  isInProduction: applicationProfile.inProduction,
  isSwaggerEnabled: applicationProfile.isSwaggerEnabled
});

const mapDispatchToProps = {setLocale, getSession, getProfile};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(hot(module)(App));
