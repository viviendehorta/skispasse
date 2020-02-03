import './olmap.scss';
import Map from 'ol/Map';
import React from "react";
import VerticalCollapse from "app/components/commons/vertical-collapse";
import {Vector as VectorSource} from "ol/source";
import {buildMarkerVectorLayer, buildOlView, buildOSMTileLayer, extractMarkerFeatures} from "app/utils/map-utils";

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
    const view = buildOlView([270000, 6250000], 1);
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

  private buildBaseLayers() {
    const oSMLayer = buildOSMTileLayer();
    this.markerVectorSource = new VectorSource({
      features: []
    });
    const markerLayer = buildMarkerVectorLayer(this.markerVectorSource);
    return [oSMLayer, markerLayer];
  }

  private updateNewsFacts(newsFacts: any[]) {
    this.markerVectorSource.addFeatures(extractMarkerFeatures(newsFacts));
  }
}
