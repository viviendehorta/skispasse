import './worldmap.scss';
import React from 'react';
import {connect} from 'react-redux';
import {OlMap} from "app/shared/map/olmap";
import {MarkerLayer} from "app/shared/map/layers/marker-layer";
import {OSMLayer} from "app/shared/map/layers/osm-layer";

export type IWorldMapPageProp = StateProps;

export const WorldMapPage = (props: IWorldMapPageProp) => {

  // initialize attributes with useState ? useState() fct trop cool

  return (
    <OlMap id="worldmap-page-map">
      <OSMLayer/>
      <MarkerLayer/>
    </OlMap>
  );
};

const mapStateToProps = storeState => ({
  // account: storeState.authentication.account,
  // isAuthenticated: storeState.authentication.isAuthenticated
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(WorldMapPage);
