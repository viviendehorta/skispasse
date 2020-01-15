import './worldmap.scss';
import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {OlMap} from "app/shared/map/olmap";
import {MarkerLayer} from "app/shared/map/layers/marker-layer";
import {OSMLayer} from "app/shared/map/layers/osm-layer";
import {getNewsFactsBlob} from "app/modules/administration/administration.reducer";
import {IRootState} from "app/shared/reducers";

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  useEffect(() => {
    props.getNewsFactsBlob();
  }, []);

  return (
    <div>
      <OlMap id="worldmap-page-map">
        <OSMLayer/>
        <MarkerLayer/>
      </OlMap>
    </div>
    // initialize attributes with useState ? useState() fct trop cool

  );
};

const mapStateToProps = (state:IRootState) => ({
  newsFactsBlob: state.administration.newsFactsBlob
});

const mapDispatchToProps = { getNewsFactsBlob };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorldMapPage);


