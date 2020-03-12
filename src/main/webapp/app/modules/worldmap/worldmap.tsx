import './worldmap.scss';
import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {IRootState} from "app/config/root.reducer";
import {fetchAllNewsFacts} from "app/modules/worldmap/news-facts.reducer";
import {buildMarkerVectorLayer, buildOlView, buildOSMTileLayer} from "app/utils/map-utils";
import Map from "ol/Map";
import NewsFactDetailModal from "app/modules/worldmap/news-fact-detail-modal";

import {Col, Row} from 'reactstrap';

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  const MAP_ID = "worldmap-page-newsFactsMap";

  const ICON_PIXEL_CLICK_TOLERANCE = 3;

  const newsCategories = [
    {
      id: 1,
      label: 'Démonstration',
    },
    {
      id: 2,
      label: 'Sport',
    },
    {
      id: 3,
      label: 'Culture',
    },
    {
      id: 4,
      label: 'Show',
    },
    {
      id: 5,
      label: 'Nature',
    },
    {
      id: 6,
      label: 'Other',
    }
  ];

  const newsCategoryElements = newsCategories.map((newsCategory) =>
    <div key={newsCategory.id} className="form-check-inline">
      <input className="form-check-input" type="checkbox" value="" id={"inputCategory" + newsCategory.id}/>
        <label className="form-check-label" htmlFor={"inputCategory" + newsCategory.id}>
          {newsCategory.label}
        </label>
    </div>

  );

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

  const buildNewsFactsMap = (newsFacts:[]) => {
    const view = buildOlView([270000, 6250000], 1);
    const oSMLayer = buildOSMTileLayer();
    const markerLayer = buildMarkerVectorLayer(newsFacts);

    const map = new Map({
      layers: [oSMLayer, markerLayer],
      target: MAP_ID,
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

    if (props.allNewsFacts) {
      buildNewsFactsMap(props.allNewsFacts);
    }

    return function cleanMapObject() {
      if (newsFactsMap) {
        newsFactsMap.setTarget(null);
      }
    };
  }, [props.allNewsFacts]);

  return (
    <Row id="worldMap">
      <Col id={MAP_ID} className="map" xs="10"/>
      <Col className="control-panel" xs="2">
        <div className="card">
          <div className="card-body">
            <h5 className="card-title">Categories</h5>
            <div className="form-check">
              {newsCategoryElements}
            </div>
          </div>
        </div>
      </Col>
      <NewsFactDetailModal newsFactId={currentNewsFactId} handleClose={handleModalDetailClose}
                           showModal={showModal}/>
    </Row>
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
