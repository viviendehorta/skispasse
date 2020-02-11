import './newsfact-detail-modal.scss';
import React, {useEffect} from 'react';
import {Button, Form, FormGroup, Label, Modal, ModalBody, ModalFooter} from 'reactstrap';
import {BigPlayButton, Player} from 'video-react';
import LoadingBar from "react-redux-loading-bar";
import {IRootState} from "app/config/root.reducer";
import {getNewsFactDetail} from "app/modules/worldmap/news-facts.reducer";
import {connect} from "react-redux";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

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

  const newsFactDetail = props.newsFactDetail;

  return (
    // <Modal
    //   className="right-modal"
    //   isOpen={props.showModal}
    //   modalTransition={{timeout: 200}} // Set same value to scss property $rightTransition
    //   backdropTransition={{timeout: 10}}
    //   toggle={props.handleClose}>
    //   <ModalHeader toggle={props.handleClose}>
    //     Detail
    //   </ModalHeader>
    //
    //   <LoadingBar className="loading-bar"/>
    //
    //   <ModalBody>
    //     {props.newsFactDetail && (
    //       <Player>
    //         <source src={props.newsFactDetail.videoPath}/>
    //       </Player>
    //     )}
    //   </ModalBody>
    // </Modal>

    <Modal
      className="news-facts-modal"
      isOpen={props.showModal}
      modalTransition={{timeout: 200}} // Set same value to scss property $rightTransition
      backdropTransition={{timeout: 10}}
      toggle={props.handleClose}
      centered={true}
    >

      <LoadingBar className="loading-bar"/>

      <ModalBody>
        {newsFactDetail && (
          <div>
            <Player playsInline>
              <BigPlayButton position="center"/>
              <source src={newsFactDetail.videoPath}/>
            </Player>
            <Form>
              <FormGroup>
                <FontAwesomeIcon icon="globe" color="#4285F4" fixedWidth/>
                <Label>{newsFactDetail.city}, {newsFactDetail.country}</Label>
              </FormGroup>
              <FormGroup>
                <FontAwesomeIcon icon="map-marker-alt" color="#4285F4" fixedWidth/>
                <Label>{newsFactDetail.address}</Label>
              </FormGroup>
            </Form>
          </div>

        )}

      </ModalBody>

      <ModalFooter>
        <Button>
          <FontAwesomeIcon icon={['fab', 'facebook-f']} fixedWidth/>
        </Button>
        <Button>
          <FontAwesomeIcon icon={['fab', 'twitter']}/>
        </Button>
        <Button data-dismiss="modal">
          Close
        </Button>

      </ModalFooter>


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
