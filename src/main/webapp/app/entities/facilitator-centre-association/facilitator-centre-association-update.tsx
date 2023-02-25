import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFacilitator } from 'app/shared/model/facilitator.model';
import { getEntities as getFacilitators } from 'app/entities/facilitator/facilitator.reducer';
import { ICentre } from 'app/shared/model/centre.model';
import { getEntities as getCentres } from 'app/entities/centre/centre.reducer';
import { IFacilitatorCentreAssociation } from 'app/shared/model/facilitator-centre-association.model';
import { getEntity, updateEntity, createEntity, reset } from './facilitator-centre-association.reducer';

export const FacilitatorCentreAssociationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const facilitators = useAppSelector(state => state.facilitator.entities);
  const centres = useAppSelector(state => state.centre.entities);
  const facilitatorCentreAssociationEntity = useAppSelector(state => state.facilitatorCentreAssociation.entity);
  const loading = useAppSelector(state => state.facilitatorCentreAssociation.loading);
  const updating = useAppSelector(state => state.facilitatorCentreAssociation.updating);
  const updateSuccess = useAppSelector(state => state.facilitatorCentreAssociation.updateSuccess);

  const handleClose = () => {
    navigate('/facilitator-centre-association');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFacilitators({}));
    dispatch(getCentres({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);
    values.joinDate = convertDateTimeToServer(values.joinDate);

    const entity = {
      ...facilitatorCentreAssociationEntity,
      ...values,
      facilitator: facilitators.find(it => it.id.toString() === values.facilitator.toString()),
      centre: centres.find(it => it.id.toString() === values.centre.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createTimeStamp: displayDefaultDateTime(),
          joinDate: displayDefaultDateTime(),
        }
      : {
          ...facilitatorCentreAssociationEntity,
          createTimeStamp: convertDateTimeFromServer(facilitatorCentreAssociationEntity.createTimeStamp),
          joinDate: convertDateTimeFromServer(facilitatorCentreAssociationEntity.joinDate),
          facilitator: facilitatorCentreAssociationEntity?.facilitator?.id,
          centre: facilitatorCentreAssociationEntity?.centre?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="danakBackendApp.facilitatorCentreAssociation.home.createOrEditLabel"
            data-cy="FacilitatorCentreAssociationCreateUpdateHeading"
          >
            Create or edit a Facilitator Centre Association
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="facilitator-centre-association-id"
                  label="ID"
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label="Create Time Stamp"
                id="facilitator-centre-association-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Join Date"
                id="facilitator-centre-association-joinDate"
                name="joinDate"
                data-cy="joinDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="facilitator-centre-association-facilitator"
                name="facilitator"
                data-cy="facilitator"
                label="Facilitator"
                type="select"
              >
                <option value="" key="0" />
                {facilitators
                  ? facilitators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="facilitator-centre-association-centre" name="centre" data-cy="centre" label="Centre" type="select">
                <option value="" key="0" />
                {centres
                  ? centres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/facilitator-centre-association"
                replace
                color="info"
              >
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FacilitatorCentreAssociationUpdate;
