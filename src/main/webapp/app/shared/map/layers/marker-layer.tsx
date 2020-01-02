import * as React from 'react';
import {Vector as VectorLayer} from "ol/layer";
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import {Icon, Style} from "ol/style";
import VectorSource from "ol/source/Vector";
import {MapContext} from "app/shared/map/olmap";

export class MarkerLayer extends React.Component<any, any> {

  static contextType = MapContext;

  constructor(props) {
    super(props);
  }

  render() {
    return null;
  }

  componentDidMount() {

    const coord1 = [-5701523.274225562, -3508003.9130105707];
    const coord2 = [270000, 6250000];

    const markers: Feature[] = [
      new Feature({geometry: new Point(coord1)}),
      new Feature({geometry: new Point(coord2)})
    ];

    const style = new Style({
      image: new Icon({
        anchor: [0.5, 1],
        src: 'content/images/map/icon.png'
      })
    });

    const markerLayer = new VectorLayer({
      source: new VectorSource({
        features: markers
      }),
      style
    });

    const parentOlMap = this.context;
    parentOlMap.addLayer(markerLayer);
  }
}
