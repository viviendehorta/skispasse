import './worldmap.scss';
import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {IRootState} from "app/config/root.reducer";
import {fetchNewsFactsBlob} from "app/modules/worldmap/news-facts-blob.reducer";
import VerticalCollapse from "app/components/commons/vertical-collapse";
import {buildMarkerVectorLayer, buildOlView, buildOSMTileLayer} from "app/utils/map-utils";
import Map from "ol/Map";

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  const WORLDMAP_MAP_ID = "worldmap-page-map";

  // Fetch news facts only after first rendering
  useEffect(() => {
    props.fetchNewsFactsBlob();
  }, []);

  // Construct the map using fetched news facts
  useEffect(() => {

    function buildLayers(newsFactsBlob: any) {
      const oSMLayer = buildOSMTileLayer();
      const markerLayer = buildMarkerVectorLayer(newsFactsBlob.newsFacts);
      return [oSMLayer, markerLayer];
    }

    function buildWorldMap(newsFactsBlob) {
      const view = buildOlView([270000, 6250000], 1);
      const layers = buildLayers(newsFactsBlob);

      const map = new Map({
        layers,
        target: WORLDMAP_MAP_ID,
        view
      });

      // this.map.on('click', function (event) {
      //   this.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
      //     alert("marker clicked !")
      //   });
      // });
    }

    const isReadyNewsFactsBLob = props.newsFactsBlob != null && props.newsFactsBlob.newsFacts != null;

    if (isReadyNewsFactsBLob) {
      buildWorldMap(props.newsFactsBlob);
    }
  }, [props.newsFactsBlob]);

  return (
    <div id="worldMap">
      <div id={WORLDMAP_MAP_ID} className="map"/>
      <VerticalCollapse/>
    </div>
  );
};

function mapStateToProps(state: IRootState) {
  return {
    isFetching: state.newsFactsBlobState.isFetching,
    newsFactsBlob: state.newsFactsBlobState.newsFactsBlob
  }
}

const mapDispatchToProps = {fetchNewsFactsBlob};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorldMapPage);
