import './worldmap.scss';
import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {IRootState} from "app/config/root.reducer";
import {fetchNewsFactsBlob} from "app/modules/worldmap/news-facts-blob.reducer";
import {buildMarkerVectorLayer, buildOlView, buildOSMTileLayer} from "app/utils/map-utils";
import Map from "ol/Map";
import NewsFactDetailModal from "app/modules/worldmap/news-fact-detail-modal";
import {Vector as VectorLayer} from 'ol/layer';

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  const WORLDMAP_MAP_ID = "worldmap-page-newsFactsMap";

  const [newsFactsMap, setNewsFactsMap] = useState(null); // News facts newsFactsMap
  const [newsFactDetail, setNewsFactDetail] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const handleModalDetailClose = () => setShowModal(false);

  // Fetch news facts only after first rendering
  useEffect(() => {
    props.fetchNewsFactsBlob();
  }, []);

  // Construct the newsFactsMap using fetched news facts
  useEffect(() => {

    const displayNewsFactDetailIfNeeded = (pixel: any, newsFactsVecorLayer: VectorLayer) => {

      newsFactsVecorLayer.getFeatures(pixel).then(function (features) {
        if (features.length) {
          const newsFactFeature = features[0];
          setNewsFactDetail(null); // todo replace with clicked news fact data
          setShowModal(true);
        }
      });
    };

    function buildWorldMap(newsFactsBlob) {
      const view = buildOlView([270000, 6250000], 1);

      const oSMLayer = buildOSMTileLayer();
      const markerLayer = buildMarkerVectorLayer(newsFactsBlob.newsFacts);

      const map = new Map({
        layers: [oSMLayer, markerLayer],
        target: WORLDMAP_MAP_ID,
        view
      });
      // map.on('click', function (event) {
      //   this.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
      //     alert("marker clicked !")
      //   });
      // });

      map.on('click', function (evt) {
        displayNewsFactDetailIfNeeded(evt.pixel, markerLayer);
      });

      setNewsFactsMap(map);
    }

    const isReadyNewsFactsBLob = props.newsFactsBlob != null && props.newsFactsBlob.newsFacts != null;

    if (isReadyNewsFactsBLob) {
      buildWorldMap(props.newsFactsBlob);
    }

    return function cleanMapObject() {
      if (newsFactsMap) {
        newsFactsMap.setTarget(null);
      }
    };
  }, [props.newsFactsBlob]);

  return (
    <div id="worldMap">
      <div id={WORLDMAP_MAP_ID} className="map"/>
      <NewsFactDetailModal newsFactDetail={newsFactDetail} handleClose={handleModalDetailClose} showModal={showModal}/>
      {/*<VerticalCollapse/>*/}
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
