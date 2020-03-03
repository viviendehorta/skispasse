import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { Vector as VectorSource } from 'ol/source';
import { Icon, Style } from 'ol/style';
import { Vector as VectorLayer } from 'ol/layer';
import View from 'ol/View';

const MARKER_STYLE = new Style({
  image: new Icon({
    anchor: [0.5, 1],
    src: 'content/images/map/map-marker-red.png'
  })
});

export const buildOlView = (center: number[], zoom: number): View => {
  return new View({
    center,
    zoom
  });
};

export const toMarkerFeature = (newsFact: any): Feature => {
  const markerFeature = new Feature({
    geometry: new Point([newsFact.locationCoordinate.x, newsFact.locationCoordinate.y]),
    newsFactId: newsFact.id
  });
  markerFeature.setStyle(MARKER_STYLE);
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
