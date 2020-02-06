import './newsfact-detail-modal.scss';
import React from 'react';
import {Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

const NewsFactDetailModal = ({handleClose, newsFactDetail, showModal}) => {
  return (
    <Modal
      className="right-modal"
      isOpen={showModal}
      modalTransition={{timeout: 200}} // Set same value to scss property $rightTransition
      backdropTransition={{timeout: 10}}
      toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Detail</ModalHeader>
      <ModalBody>

      </ModalBody>
      <ModalFooter>
      </ModalFooter>
    </Modal>
  );
};

export default NewsFactDetailModal;
