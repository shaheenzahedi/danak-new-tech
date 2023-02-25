import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICentre } from 'app/shared/model/centre.model';
import { getEntities as getCentres } from 'app/entities/centre/centre.reducer';
import { IDevice } from 'app/shared/model/device.model';
import { getEntities as getDevices } from 'app/entities/device/device.reducer';
import { IFacilitator } from 'app/shared/model/facilitator.model';
import { getEntities as getFacilitators } from 'app/entities/facilitator/facilitator.reducer';
import { IChild } from 'app/shared/model/child.model';
import { getEntity, updateEntity, createEntity, reset } from './child.reducer';

export const ChildUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const centres = useAppSelector(state => state.centre.entities);
  const devices = useAppSelector(state => state.device.entities);
  const facilitators = useAppSelector(state => state.facilitator.entities);
  const childEntity = useAppSelector(state => state.child.entity);
  const loading = useAppSelector(state => state.child.loading);
  const updating = useAppSelector(state => state.child.updating);
  const updateSuccess = useAppSelector(state => state.child.updateSuccess);

  const handleClose = () => {
    navigate('/child');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCentres({}));
    dispatch(getDevices({}));
    dispatch(getFacilitators({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);

    const entity = {
      ...childEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      centre: centres.find(it => it.id.toString() === values.centre.toString()),
      device: devices.find(it => it.id.toString() === values.device.toString()),
      facilitator: facilitators.find(it => it.id.toString() === values.facilitator.toString()),
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
        }
      : {
          ...childEntity,
          createTimeStamp: convertDateTimeFromServer(childEntity.createTimeStamp),
          user: childEntity?.user?.id,
          centre: childEntity?.centre?.id,
          device: childEntity?.device?.id,
          facilitator: childEntity?.facilitator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakBackendApp.child.home.createOrEditLabel" data-cy="ChildCreateUpdateHeading">
            Create or edit a Child
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="child-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Create Time Stamp"
                id="child-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="child-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-centre" name="centre" data-cy="centre" label="Centre" type="select">
                <option value="" key="0" />
                {centres
                  ? centres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-device" name="device" data-cy="device" label="Device" type="select">
                <option value="" key="0" />
                {devices
                  ? devices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-facilitator" name="facilitator" data-cy="facilitator" label="Facilitator" type="select">
                <option value="" key="0" />
                {facilitators
                  ? facilitators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/child" replace color="info">
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

export default ChildUpdate;
