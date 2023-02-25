import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IDevice } from 'app/shared/model/device.model';
import { getEntity, updateEntity, createEntity, reset } from './device.reducer';

export const DeviceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cities = useAppSelector(state => state.city.entities);
  const deviceEntity = useAppSelector(state => state.device.entity);
  const loading = useAppSelector(state => state.device.loading);
  const updating = useAppSelector(state => state.device.updating);
  const updateSuccess = useAppSelector(state => state.device.updateSuccess);

  const handleClose = () => {
    navigate('/device');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);

    const entity = {
      ...deviceEntity,
      ...values,
      city: cities.find(it => it.id.toString() === values.city.toString()),
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
          ...deviceEntity,
          createTimeStamp: convertDateTimeFromServer(deviceEntity.createTimeStamp),
          city: deviceEntity?.city?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakBackendApp.device.home.createOrEditLabel" data-cy="DeviceCreateUpdateHeading">
            Create or edit a Device
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="device-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Create Time Stamp"
                id="device-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Universal Id" id="device-universalId" name="universalId" data-cy="universalId" type="text" />
              <ValidatedField label="Global Num" id="device-globalNum" name="globalNum" data-cy="globalNum" type="text" />
              <ValidatedField label="Model" id="device-model" name="model" data-cy="model" type="text" />
              <ValidatedField label="Year Built" id="device-yearBuilt" name="yearBuilt" data-cy="yearBuilt" type="text" />
              <ValidatedField label="Android Id" id="device-androidId" name="androidId" data-cy="androidId" type="text" />
              <ValidatedField id="device-city" name="city" data-cy="city" label="City" type="select">
                <option value="" key="0" />
                {cities
                  ? cities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/device" replace color="info">
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

export default DeviceUpdate;
