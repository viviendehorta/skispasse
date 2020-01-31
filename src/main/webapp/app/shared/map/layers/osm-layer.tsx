import * as React from 'react';
import {Tile as TileLayer} from "ol/layer";
import OSM from "ol/source/OSM";

export class OSMLayer extends React.Component<any, any> {

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
