import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChild } from 'app/shared/model/child.model';
import { getEntities as getChildren } from 'app/entities/child/child.reducer';
import { IDevice } from 'app/shared/model/device.model';
import { getEntities as getDevices } from 'app/entities/device/device.reducer';
import { ISingleUnit } from 'app/shared/model/single-unit.model';
import { getEntities as getSingleUnits } from 'app/entities/single-unit/single-unit.reducer';
import { IProgress } from 'app/shared/model/progress.model';
import { getEntity, updateEntity, createEntity, reset } from './progress.reducer';

export const ProgressUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const children = useAppSelector(state => state.child.entities);
  const devices = useAppSelector(state => state.device.entities);
  const singleUnits = useAppSelector(state => state.singleUnit.entities);
  const progressEntity = useAppSelector(state => state.progress.entity);
  const loading = useAppSelector(state => state.progress.loading);
  const updating = useAppSelector(state => state.progress.updating);
  const updateSuccess = useAppSelector(state => state.progress.updateSuccess);

  const handleClose = () => {
    navigate('/progress');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getChildren({}));
    dispatch(getDevices({}));
    dispatch(getSingleUnits({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);

    const entity = {
      ...progressEntity,
      ...values,
      child: children.find(it => it.id.toString() === values.child.toString()),
      createdByDevice: devices.find(it => it.id.toString() === values.createdByDevice.toString()),
      singleUnit: singleUnits.find(it => it.id.toString() === values.singleUnit.toString()),
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
          ...progressEntity,
          createTimeStamp: convertDateTimeFromServer(progressEntity.createTimeStamp),
          child: progressEntity?.child?.id,
          createdByDevice: progressEntity?.createdByDevice?.id,
          singleUnit: progressEntity?.singleUnit?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakBackendApp.progress.home.createOrEditLabel" data-cy="ProgressCreateUpdateHeading">
            Create or edit a Progress
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="progress-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Create Time Stamp"
                id="progress-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Spent Time" id="progress-spentTime" name="spentTime" data-cy="spentTime" type="text" />
              <ValidatedField id="progress-child" name="child" data-cy="child" label="Child" type="select">
                <option value="" key="0" />
                {children
                  ? children.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="progress-createdByDevice"
                name="createdByDevice"
                data-cy="createdByDevice"
                label="Created By Device"
                type="select"
              >
                <option value="" key="0" />
                {devices
                  ? devices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="progress-singleUnit" name="singleUnit" data-cy="singleUnit" label="Single Unit" type="select">
                <option value="" key="0" />
                {singleUnits
                  ? singleUnits.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/progress" replace color="info">
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

export default ProgressUpdate;
