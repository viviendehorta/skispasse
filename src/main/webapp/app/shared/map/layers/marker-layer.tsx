import * as React from 'react';
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import {MapContext} from "app/shared/map/olmap";
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer';
import {Cluster, OSM, Vector as VectorSource} from 'ol/source';
import {Circle as CircleStyle, Fill, Icon, Stroke, Style, Text} from 'ol/style';

export class MarkerLayer extends React.Component<any, any> {

  static contextType = MapContext;

  constructor(props) {
    super(props);
  }

  render() {
    return null;
  }

  componentDidMount() {

    const style = new Style({
      image: new Icon({
        anchor: [0.5, 1],
        src: 'content/images/map/icon.png'
      })
    });

    const markerLayer = this.buildLayer();

    const parentOlMap = this.context;
    parentOlMap.addLayer(markerLayer);
  }

  private buildLayer() {

    const count = 20000;
    const features = new Array(count);
    const e = 4500000;
    for(let i = 0; i < count; ++i) {
      const coordinates = [2 * e * Math.random() - e, 2 * e * Math.random() - e];
      features[i] = new Feature(new Point(coordinates));
    }

    const source = new VectorSource({
      features
    });

    const clusterSource = new Cluster({
      distance: 70,
      source
    });

    const styleCache = {};
    const clusters = new VectorLayer({
      source: clusterSource,
      style(feature) {
        const size = feature.get('features').length;
        let style = styleCache[size];
        if (!style) {
          style = new Style({
            image: new CircleStyle({
              radius: 17,
              stroke: new Stroke({
                color: '#fff'
              }),
              fill: new Fill({
                color: '#3399CC'
              })
            }),
            text: new Text({
              text: size.toString(),
              fill: new Fill({
                color: '#fff'
              })
            })
          });
          styleCache[size] = style;
        }
        return style;
      }
    });

    // distance.addEventListener('input', function () {
    //   clusterSource.setDistance(parseInt(distance.value, 10));
    // });


    return clusters;
  }
}
