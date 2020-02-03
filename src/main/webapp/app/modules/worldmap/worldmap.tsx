import './worldmap.scss';
import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {OlMap} from "app/components/map/olmap";
import {IRootState} from "app/config/root.reducer";
import {fetchNewsFactsBlob} from "app/components/map/news-facts-blob.reducer";

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  useEffect(() => {
    props.fetchNewsFactsBlob();
  }, []);

  const newsFactsBlob = props.newsFactsBlob;
  const isReadyNewsFactsBLob = newsFactsBlob != null && newsFactsBlob.newsFacts != null;

  return(
      <div>
        <OlMap id="worldmap-page-map" newsFacts={isReadyNewsFactsBLob ? newsFactsBlob.newsFacts : []}/>
      </div>
    );
};

function mapStateToProps(state: IRootState) {
  return {
    isFetching: state.newsFactsBlobState.isFetching,
    newsFactsBlob: state.newsFactsBlobState.newsFactsBlob
  }
}

const mapDispatchToProps = {fetchNewsFactsBlob};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorldMapPage);
