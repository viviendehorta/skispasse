import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { Vector as VectorSource } from 'ol/source';
import { Icon, Style } from 'ol/style';
import { Vector as VectorLayer } from 'ol/layer';
import View from 'ol/View';

const DEMONSTRATION_MARKER_STYLE = new Style({
  image: new Icon({
    anchor: [0.5, 1],
    src: 'content/images/map/news-category-demonstration.svg'
  })
});

const SPORT_MARKER_STYLE = new Style({
  image: new Icon({
    anchor: [0.5, 1],
    src: 'content/images/map/news-category-sport.svg'
  })
});

const CULTURE_MARKER_STYLE = new Style({
  image: new Icon({
    anchor: [0.5, 1],
    src: 'content/images/map/news-category-culture.svg'
  })
});

const SHOW_MARKER_STYLE = new Style({
  image: new Icon({
    anchor: [0.5, 1],
    src: 'content/images/map/news-category-show.svg'
  })
});

const NATURE_MARKER_STYLE = new Style({
  image: new Icon({
    anchor: [0.5, 1],
    src: 'content/images/map/news-category-nature.svg'
  })
});

const OTHER_MARKER_STYLE = new Style({
  image: new Icon({
    anchor: [0.5, 1],
    src: 'content/images/map/news-category-other.svg'
  })
});

export const buildOlView = (center: number[], zoom: number): View => {
  return new View({
    center,
    zoom
  });
};

export const getMarkerStyle = (newsFact: any) => {
  switch (newsFact.category) {
    case 'DEMONSTRATION':
      return DEMONSTRATION_MARKER_STYLE;
    case 'SPORT':
      return SPORT_MARKER_STYLE;
    case 'CULTURE':
      return CULTURE_MARKER_STYLE;
    case 'SHOW':
      return SHOW_MARKER_STYLE;
    case 'NATURE':
      return NATURE_MARKER_STYLE;
    case 'OTHER':
      return OTHER_MARKER_STYLE;
    default:
      return OTHER_MARKER_STYLE;
  }
};

export const toMarkerFeature = (newsFact: any): Feature => {
  const markerFeature = new Feature({
    geometry: new Point([newsFact.locationCoordinate.x, newsFact.locationCoordinate.y]),
    newsFactId: newsFact.id
  });
  markerFeature.setStyle(getMarkerStyle(newsFact));
  return markerFeature;
};

export const extractMarkerFeatures = (newsFacts: any[]): Feature[] => {
  return newsFacts.map(newsFact => {
    return toMarkerFeature(newsFact);
  });
};

export const buildOSMTileLayer = () => {
  return new TileLayer({
    source: new OSM()
  });
};

export const buildMarkerVectorLayer = (newsFacts: any[]): VectorLayer => {
  const vectorSource = new VectorSource({
    features: extractMarkerFeatures(newsFacts)
  });
  return new VectorLayer({
    source: vectorSource
  });
};
