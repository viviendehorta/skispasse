import './olmap.scss';
import Map from 'ol/Map';
import View from 'ol/View';
import React from "react";
import VerticalCollapse from "app/shared/components/vertical-collapse";

export const MapContext = React.createContext(undefined);

export class OlMap extends React.Component<any, any> {

  private map: Map;
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
      <MapContext.Provider value={this}>
        <div className="olmap">
          <div id={this.props.id} className="map">
            {this.props.children}
          </div>
          <VerticalCollapse/>
        </div>
      </MapContext.Provider>
    );
  }

  componentDidMount() {

    const coordCenter = [270000, 6250000];
    this.view = this.buildView(coordCenter)

    this.map = new Map({
      layers: this.layers,
      target: this.props.id,
      view: this.view
    });

    // this.map.on('click', function (event) {
    //   this.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
    //     alert("marker clicked !")
    //   });
    // });
  }

  private buildView(center: number[]) {
    return new View({
      center,
      zoom: 3
    });
  }

  addLayer(layer) {
    this.layers.push(layer);
  }
}
