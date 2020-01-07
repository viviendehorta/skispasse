import './worldmap.scss';
import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {OlMap} from "app/shared/map/olmap";
import {MarkerLayer} from "app/shared/map/layers/marker-layer";
import {OSMLayer} from "app/shared/map/layers/osm-layer";
import {IRootState} from "app/shared/reducers";
import {newsFactsBlob} from "app/modules/administration/administration.reducer";

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  useEffect(() => {
    props.newsFactsBlob();
  }, []);

  const getNewsFactsBlob = () => {
    if (!props.isFetching) {
      props.newsFactsBlob();
    }
  };

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

const mapStateToProps = (storeState: IRootState) => ({
  newsFactsBlob: storeState.administration.newsFactsBlob,
  isFetching: storeState.administration.loading
});

const mapDispatchToProps = { newsFactsBlob };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorldMapPage);


