import './worldmap.scss';
import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {OlMap} from "app/shared/map/olmap";
import {IRootState} from "app/shared/reducers";
import {fetchNewsFactsBlob} from "app/shared/reducers/news-facts-blob";
import {Spinner} from 'reactstrap';

export interface IWorldMapPageProps extends StateProps, DispatchProps {
}

export const WorldMapPage = (props: IWorldMapPageProps) => {

  useEffect(() => {
    props.fetchNewsFactsBlob();
  }, []);

  const newsFactsBlob = props.newsFactsBlob;
  const isReadyNewsFactsBLob = newsFactsBlob != null && newsFactsBlob.newsFacts != null;

  return isReadyNewsFactsBLob ?
    (
      <div>
        <OlMap id="worldmap-page-map" newsFactsBlob={newsFactsBlob}/>
      </div>
    ) :
    (
      <Spinner />
    );
}

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
