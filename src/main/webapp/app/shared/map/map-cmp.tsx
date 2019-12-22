import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import React from "react";

export interface IMapCmpProps {
  id: string
}

export class MapCmp extends React.Component<IMapCmpProps> {

  render() {
    return (
      <div id={this.props.id} className="map"/>
    );
  }

  componentDidMount() {
    const wMap = new Map({
      layers: [
        new TileLayer({
          source: new OSM()
        })
      ],
      target: this.props.id,
      view: new View({
        center: [0, 0],
        zoom: 2
      })
    });
  }
}
