import './worldmap.scss';
import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {IRootState} from "app/config/root.reducer";
import {fetchNewsFactsBlob} from "app/modules/worldmap/news-facts-blob.reducer";
import {buildMarkerVectorLayer, buildOlView, buildOSMTileLayer} from "app/utils/map-utils";
import Map from "ol/Map";
import NewsFactDetailModal from "app/modules/worldmap/news-fact-detail-modal";

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  const WORLDMAP_MAP_ID = "worldmap-page-newsFactsMap";

  const [newsFactsMap, setNewsFactsMap] = useState(null); // News facts newsFactsMap
  const [currentNewsFactDetail, setCurrentNewsFactDetail] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const handleModalDetailClose = () => setShowModal(false);

  // Fetch news facts only after first rendering
  useEffect(() => {
    props.fetchNewsFactsBlob();
  }, []);

  // TODO action getNewsFact
  const getNewsFactDetail = (newsFactId: number) => {
    return {
      id: 101,
      date: "2020-01-10",
      time: "10h46m",
      newsCategory: "culture", // Politic, culture, etc.
      location: "Place de la République, Paris",
      videoPath: "/content/video/small.mp4" // TODO doit être une url sur le serveur
    };
  };

  function showNewsFactDetail(newsFactId: number) {
    const newsFactDetail = getNewsFactDetail(newsFactId)
    setCurrentNewsFactDetail(newsFactDetail); // todo replace with clicked news fact data
    setShowModal(true);
  }

  const buildNewsFactsdMap = (newsFactsBlob) => {
    const view = buildOlView([270000, 6250000], 1);
    const oSMLayer = buildOSMTileLayer();
    const markerLayer = buildMarkerVectorLayer(newsFactsBlob.newsFacts);

    const map = new Map({
      layers: [oSMLayer, markerLayer],
      target: WORLDMAP_MAP_ID,
      view
    });

    // Behaviour when new fact markers are clicked : displaying detail modal
    map.on('click', function (evt) {
      this.forEachFeatureAtPixel(evt.pixel, function (clickedNewsFactFeature, layer) {
        showNewsFactDetail(clickedNewsFactFeature.get("newsFactId"));
        return true; // Returns true to stop feature iteration if there was several on the same pixel
      }, {
        layerFilter(layerCandidate) {
          return layerCandidate === markerLayer;
        }
      });
    });

    setNewsFactsMap(map);
  };

  // Create the news facts Map using fetched news facts
  useEffect(() => {

    const isReadyNewsFactsBLob = props.newsFactsBlob != null && props.newsFactsBlob.newsFacts != null;

    if (isReadyNewsFactsBLob) {
      buildNewsFactsdMap(props.newsFactsBlob);
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
      <NewsFactDetailModal newsFactDetail={currentNewsFactDetail} handleClose={handleModalDetailClose}
                           showModal={showModal}/>
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
