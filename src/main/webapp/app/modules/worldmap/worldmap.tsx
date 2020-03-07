import './worldmap.scss';
import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {IRootState} from "app/config/root.reducer";
import {fetchAllNewsFacts} from "app/modules/worldmap/news-facts.reducer";
import {buildMarkerVectorLayer, buildOlView, buildOSMTileLayer} from "app/utils/map-utils";
import Map from "ol/Map";
import NewsFactDetailModal from "app/modules/worldmap/news-fact-detail-modal";

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  const WORLDMAP_MAP_ID = "worldmap-page-newsFactsMap";
  const ICON_PIXEL_CLICK_TOLERANCE = 3;

  const [newsFactsMap, setNewsFactsMap] = useState(null); // News facts newsFactsMap
  const [currentNewsFactId, setCurrentNewsFactId] = useState(0);
  const [showModal, setShowModal] = useState(false);

  const handleModalDetailClose = () => {
    setShowModal(false);
  };

  // Fetch news facts only after first rendering
  useEffect(() => {
    props.fetchAllNewsFacts();
  }, []);

  const showNewsFactDetail = (newsFactId: number) => {
    setCurrentNewsFactId(newsFactId);
    setShowModal(true);
  };

  const buildNewsFactsdMap = (newsFacts) => {
    const view = buildOlView([270000, 6250000], 1);
    const oSMLayer = buildOSMTileLayer();
    const markerLayer = buildMarkerVectorLayer(newsFacts);

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
        },
        hitTolerance: ICON_PIXEL_CLICK_TOLERANCE
      });
    });

    setNewsFactsMap(map);
  };

  // Create the news facts Map using fetched news facts
  useEffect(() => {

    const isReadyNewsFactsBLob = props.allNewsFacts;

    if (isReadyNewsFactsBLob) {
      buildNewsFactsdMap(props.allNewsFacts);
    }

    return function cleanMapObject() {
      if (newsFactsMap) {
        newsFactsMap.setTarget(null);
      }
    };
  }, [props.allNewsFacts]);

  return (
    <div id="worldMap">
      <div id={WORLDMAP_MAP_ID} className="map"/>
      <NewsFactDetailModal newsFactId={currentNewsFactId} handleClose={handleModalDetailClose}
                           showModal={showModal}/>
    </div>
  );
};

function mapStateToProps(state: IRootState) {
  return {
    allNewsFacts: state.newsFactsState.allNewsFacts,
  };
}

const mapDispatchToProps = {fetchAllNewsFacts};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorldMapPage);
