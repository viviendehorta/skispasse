import './olmap.scss';
import Map from 'ol/Map';
import View from 'ol/View';
import React from "react";
import VerticalCollapse from "app/shared/components/vertical-collapse";
import Feature from "ol/Feature";
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import {Vector as VectorSource} from "ol/source";
import {Vector as VectorLayer} from "ol/layer";
import {Icon, Style} from "ol/style";
import Point from "ol/geom/Point";

export interface IOlMapProps {
  id: string,
  newsFactsBlob: any,
}

export class OlMap extends React.Component<IOlMapProps, any> {

  private map: Map;

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="olmap">
        <div id={this.props.id} className="map"/>
        <VerticalCollapse/>
      </div>
    );
  }

  private toMarkerFeature(newsFact: any) {
    return new Feature(new Point([newsFact.locationCoordinate.x, newsFact.locationCoordinate.y]))
  }

  private extractMarkers(newsFacts: any[]): Feature[] {
    return newsFacts.map(newsFact => {
      return this.toMarkerFeature(newsFact);
    });
  }

  private buildMarkerVectorLayer() {

    const markerStyle = new Style({
      image: new Icon({
        anchor: [0.5, 1],
        src: 'content/images/map/icon.png'
      })
    });

    const markerFeatures = this.extractMarkers(this.props.newsFactsBlob.newsFacts);
    const markerSource = new VectorSource({
      features: markerFeatures
    });

    return new VectorLayer({
      source: markerSource,
      style: markerStyle
    });
  }

  private buildView() {
    return new View({
      center: [270000, 6250000],
      zoom: 3
    });
  }

  private buildOSMTileLayer() {
    return new TileLayer({
      source: new OSM()
    });
  }

  private buildLayers() {
    const oSMLayer = this.buildOSMTileLayer();
    const markerLayer = this.buildMarkerVectorLayer();
    return [oSMLayer, markerLayer];
  }

  componentDidMount() {
    const view = this.buildView();
    const layers = this.buildLayers();

    this.map = new Map({
      layers,
      target: this.props.id,
      view
    });

    // this.map.on('click', function (event) {
    //   this.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
    //     alert("marker clicked !")
    //   });
    // });
  }
}
