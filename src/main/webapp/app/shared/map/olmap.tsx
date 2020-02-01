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
  newsFacts: any[],
}

export class OlMap extends React.Component<IOlMapProps, any> {

  private map: Map;
  private markerVectorSource: VectorSource;

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

  componentDidMount() {
    const view = this.buildView();
    const layers = this.buildBaseLayers();

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

  componentDidUpdate(prevProps: Readonly<IOlMapProps>, prevState: Readonly<any>, snapshot?: any): void {
    this.updateNewsFacts(this.props.newsFacts);
  }

  componentWillUnmount() {
    this.map.setTarget(undefined)
  }

  private toMarkerFeature(newsFact: any) {
    return new Feature(new Point([newsFact.locationCoordinate.x, newsFact.locationCoordinate.y]))
  }

  private extractMarkerFeatures(newsFacts: any[]): Feature[] {
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

    this.markerVectorSource = new VectorSource({
      features: []
    });

    return new VectorLayer({
      source: this.markerVectorSource,
      style: markerStyle
    });
  }

  private buildView() {
    return new View({
      center: [270000, 6250000],
      zoom: 1
    });
  }

  private buildOSMTileLayer() {
    return new TileLayer({
      source: new OSM()
    });
  }

  private buildBaseLayers() {
    const oSMLayer = this.buildOSMTileLayer();
    const markerLayer = this.buildMarkerVectorLayer();
    return [oSMLayer, markerLayer];
  }

  private updateNewsFacts(newsFacts: any[]) {
    this.markerVectorSource.addFeatures(this.extractMarkerFeatures(newsFacts));
  }
}
