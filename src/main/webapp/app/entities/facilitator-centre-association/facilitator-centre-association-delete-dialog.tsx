import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './facilitator-centre-association.reducer';

export const FacilitatorCentreAssociationDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const facilitatorCentreAssociationEntity = useAppSelector(state => state.facilitatorCentreAssociation.entity);
  const updateSuccess = useAppSelector(state => state.facilitatorCentreAssociation.updateSuccess);

  const handleClose = () => {
    navigate('/facilitator-centre-association');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(facilitatorCentreAssociationEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="facilitatorCentreAssociationDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="danakBackendApp.facilitatorCentreAssociation.delete.question">
        Are you sure you want to delete Facilitator Centre Association {facilitatorCentreAssociationEntity.id}?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button
          id="jhi-confirm-delete-facilitatorCentreAssociation"
          data-cy="entityConfirmDeleteButton"
          color="danger"
          onClick={confirmDelete}
        >
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default FacilitatorCentreAssociationDeleteDialog;
