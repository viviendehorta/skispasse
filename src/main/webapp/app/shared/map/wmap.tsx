import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import React from "react";

export class WMap extends React.Component {

  render() {
    return (
      <div id="wmap" className="wmap"/>
    );
  }

  componentDidMount(){
    const wMap = new Map({
      layers: [
        new TileLayer({
          source: new OSM()
        })
      ],
      target: 'wmap',
      view: new View({
        center: [0, 0],
        zoom: 2
      })
    });
  }
}
