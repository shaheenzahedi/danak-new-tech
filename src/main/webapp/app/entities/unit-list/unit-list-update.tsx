import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUnitList } from 'app/shared/model/unit-list.model';
import { UnitListType } from 'app/shared/model/enumerations/unit-list-type.model';
import { PresenterName } from 'app/shared/model/enumerations/presenter-name.model';
import { getEntity, updateEntity, createEntity, reset } from './unit-list.reducer';

export const UnitListUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const unitListEntity = useAppSelector(state => state.unitList.entity);
  const loading = useAppSelector(state => state.unitList.loading);
  const updating = useAppSelector(state => state.unitList.updating);
  const updateSuccess = useAppSelector(state => state.unitList.updateSuccess);
  const unitListTypeValues = Object.keys(UnitListType);
  const presenterNameValues = Object.keys(PresenterName);

  const handleClose = () => {
    navigate('/unit-list');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);

    const entity = {
      ...unitListEntity,
      ...values,
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
          type: 'STUDY',
          presenter: 'SAM',
          ...unitListEntity,
          createTimeStamp: convertDateTimeFromServer(unitListEntity.createTimeStamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakBackendApp.unitList.home.createOrEditLabel" data-cy="UnitListCreateUpdateHeading">
            Create or edit a Unit List
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="unit-list-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Create Time Stamp"
                id="unit-list-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Num" id="unit-list-num" name="num" data-cy="num" type="text" />
              <ValidatedField label="Display Name" id="unit-list-displayName" name="displayName" data-cy="displayName" type="text" />
              <ValidatedField label="Type" id="unit-list-type" name="type" data-cy="type" type="select">
                {unitListTypeValues.map(unitListType => (
                  <option value={unitListType} key={unitListType}>
                    {unitListType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Presenter" id="unit-list-presenter" name="presenter" data-cy="presenter" type="select">
                {presenterNameValues.map(presenterName => (
                  <option value={presenterName} key={presenterName}>
                    {presenterName}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/unit-list" replace color="info">
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

export default UnitListUpdate;
