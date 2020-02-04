import React from 'react';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

const NewsFactDetailModal = ({ handleClose, newsFactDetail, showModal }) => {
  return (
    <Modal isOpen={showModal} modalTransition={{ timeout: 20 }} backdropTransition={{ timeout: 10 }} toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Detail</ModalHeader>
      <ModalBody>

      </ModalBody>
      <ModalFooter>
        <Button color="primary" onClick={handleClose}>
          Close
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default NewsFactDetailModal;
