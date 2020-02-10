import './newsfact-detail-modal.scss';
import React, {useEffect} from 'react';
import {Modal, ModalBody, ModalHeader} from 'reactstrap';
import {Player} from 'video-react';
import LoadingBar from "react-redux-loading-bar";
import {IRootState} from "app/config/root.reducer";
import {getNewsFactDetail} from "app/modules/worldmap/news-facts.reducer";
import {connect} from "react-redux";

export interface INewsFactDetailModalProps extends StateProps, DispatchProps {
  newsFactId: number,
  handleClose,
  showModal: boolean
}

const NewsFactDetailModal = (props: INewsFactDetailModalProps) => {

  // Request the news fact detail when news fact id is updated
  useEffect(() => {
    if (props.newsFactId) {
      props.getNewsFactDetail(props.newsFactId);
    }
  }, [props.newsFactId]);

  return (
    <Modal
      className="right-modal"
      isOpen={props.showModal}
      modalTransition={{timeout: 200}} // Set same value to scss property $rightTransition
      backdropTransition={{timeout: 10}}
      toggle={props.handleClose}>
      <ModalHeader toggle={props.handleClose}>
        Detail
      </ModalHeader>

      <LoadingBar className="loading-bar"/>

      <ModalBody>
        {props.newsFactDetail && (
          <Player>
            <source src={props.newsFactDetail.videoPath}/>
          </Player>
        )}
      </ModalBody>
    </Modal>
  );
};

function mapStateToProps(state: IRootState) {
  return {
    newsFactDetail: state.newsFactsState.currentNewsFactDetail
  };
}

const mapDispatchToProps = {getNewsFactDetail};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NewsFactDetailModal);
