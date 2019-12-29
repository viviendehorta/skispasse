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

export class OlMap extends React.Component<any, any> {

  private layers: any[] = [];
  private interactions: any[] = [];
  private controls: any[] = [];
  private overlays: any[] = [];
  private view: any;

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div id={this.props.id} className="map"/>
    );
  }

  componentDidMount() {

    const coord1 = [-5701523.274225562, -3508003.9130105707];
    const coord2 = [270000, 6250000];

    this.layers = this.buildLayers(coord1, coord2);
    this.view = this.buildView(coord2)


    const map = new Map({
      layers: this.layers,
      target: this.props.id,
      view: this.view
    });

    map.on('click', function (event) {

      map.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
        alert("marker clicked !")
      });
    });
  }

  private buildView(center: number[]) {
    return new View({
      center,
      zoom: 7
    });
  }

  private buildLayers(coord1: number[], coord2: number[]) {

    const mapLayer = new TileLayer({
      source: new OSM()
    });

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

    return [mapLayer, markerLayer];
  }
}
