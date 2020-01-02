import * as React from 'react';
import {Tile as TileLayer} from "ol/layer";
import {MapContext} from "app/shared/map/olmap";
import OSM from "ol/source/OSM";

export class OSMLayer extends React.Component<any, any> {

  static contextType = MapContext;

  constructor(props) {
    super(props);
  }

  render() {
    return null;
  }

  componentDidMount() {
    const oSMLayer = new TileLayer({
      source: new OSM()
    });
    const parentOlMap = this.context;
    parentOlMap.addLayer(oSMLayer);
  }
}
