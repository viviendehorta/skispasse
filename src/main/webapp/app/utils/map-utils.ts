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
    src: 'content/images/map/icon.png'
  })
});

export const buildOlView = (center: number[], zoom: number) => {
  return new View({
    center,
    zoom
  });
};

export const buildOSMTileLayer = () => {
  return new TileLayer({
    source: new OSM()
  });
};

export const buildMarkerVectorLayer = (markerVectorSource: VectorSource) => {
  return new VectorLayer({
    source: markerVectorSource,
    style: MARKER_STYLE
  });
};

export const toMarkerFeature = (newsFact: any) => {
  return new Feature(new Point([newsFact.locationCoordinate.x, newsFact.locationCoordinate.y]));
};

export const extractMarkerFeatures = (newsFacts: any[]): Feature[] => {
  return newsFacts.map(newsFact => {
    return toMarkerFeature(newsFact);
  });
};
