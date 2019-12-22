import './worldmap.scss';

import React from 'react';
import {Link} from 'react-router-dom';
import {Translate} from 'react-jhipster';
import {connect} from 'react-redux';
import {Alert, Col, Row} from 'reactstrap';
import {MapCmp} from "app/shared/map/map-cmp";

export type IWorldMapPageProp = StateProps;

export const WorldMapPage = (props: IWorldMapPageProp) => {

  // initialize attributes with useState ? useState() fct trop cool

  return (
    <MapCmp id="map-page-map"/>
  );
};

const mapStateToProps = storeState => ({
  // account: storeState.authentication.account,
  // isAuthenticated: storeState.authentication.isAuthenticated
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(WorldMapPage);
