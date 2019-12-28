import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import React from "react";
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer';
import OSM from 'ol/source/OSM';
import {Icon, Style} from 'ol/style';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import VectorSource from 'ol/source/Vector';

export interface IOlMapProps {
  id: string
}

export class OlMap extends React.Component<IOlMapProps> {

  render() {
    return (
      <div id={this.props.id} className="map"/>
    );
  }

  componentDidMount() {

    const mapLayer = new TileLayer({
      source: new OSM()
    });

    const coord1 = [-5701523.274225562,-3508003.9130105707];
    const coord2 = [270000,6250000];

    const markers: Feature[] = [];
    markers.push(
      new Feature({geometry: new Point(coord1)}));

    markers.push(
      new Feature({geometry: new Point(coord2)}));

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

    const map = new Map({
      layers: [mapLayer, markerLayer],
      target: this.props.id,
      view: new View({
        center: coord2,
        zoom: 7
      })
    });

    map.on('click', function (event) {

      map.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
        alert("marker clicked !")
      });
    });
  }
}
