import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { Vector as VectorSource } from 'ol/source';
import { Icon, Style } from 'ol/style';
import { Vector as VectorLayer } from 'ol/layer';
import View from 'ol/View';

export const buildOlView = (center: number[], zoom: number): View => {
  return new View({
    center,
    zoom
  });
};

export const getMarkerStyle = categoryId => {
  return new Style({
    image: new Icon({
      anchor: [0.5, 1],
      src: 'content/images/news-categories/news-category' + categoryId + '.svg'
    })
  });
};

export const toMarkerFeature = (newsFact): Feature => {
  const { categoryId } = newsFact;
  const style = getMarkerStyle(categoryId);
  const markerFeature = new Feature({
    geometry: new Point([newsFact.locationCoordinate.x, newsFact.locationCoordinate.y]),
    newsFactId: newsFact.id
  });
  markerFeature.setStyle(style);
  return markerFeature;
};

export const extractMarkerFeatures = (allNewsFacts: []): Feature[] => {
  return allNewsFacts.map(newsFact => toMarkerFeature(newsFact));
};

export const buildOSMTileLayer = () => {
  return new TileLayer({
    source: new OSM()
  });
};

export const buildMarkerVectorLayer = (allNewsFacts: []): VectorLayer => {
  const vectorSource = new VectorSource({
    features: extractMarkerFeatures(allNewsFacts)
  });
  return new VectorLayer({
    source: vectorSource
  });
};
