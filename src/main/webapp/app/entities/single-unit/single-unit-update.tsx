import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUnitList } from 'app/shared/model/unit-list.model';
import { getEntities as getUnitLists } from 'app/entities/unit-list/unit-list.reducer';
import { ISingleUnit } from 'app/shared/model/single-unit.model';
import { getEntity, updateEntity, createEntity, reset } from './single-unit.reducer';

export const SingleUnitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const unitLists = useAppSelector(state => state.unitList.entities);
  const singleUnitEntity = useAppSelector(state => state.singleUnit.entity);
  const loading = useAppSelector(state => state.singleUnit.loading);
  const updating = useAppSelector(state => state.singleUnit.updating);
  const updateSuccess = useAppSelector(state => state.singleUnit.updateSuccess);

  const handleClose = () => {
    navigate('/single-unit');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUnitLists({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);

    const entity = {
      ...singleUnitEntity,
      ...values,
      unitList: unitLists.find(it => it.id.toString() === values.unitList.toString()),
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
          ...singleUnitEntity,
          createTimeStamp: convertDateTimeFromServer(singleUnitEntity.createTimeStamp),
          unitList: singleUnitEntity?.unitList?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakBackendApp.singleUnit.home.createOrEditLabel" data-cy="SingleUnitCreateUpdateHeading">
            Create or edit a Single Unit
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="single-unit-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Create Time Stamp"
                id="single-unit-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Global Num" id="single-unit-globalNum" name="globalNum" data-cy="globalNum" type="text" />
              <ValidatedField id="single-unit-unitList" name="unitList" data-cy="unitList" label="Unit List" type="select">
                <option value="" key="0" />
                {unitLists
                  ? unitLists.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/single-unit" replace color="info">
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

export default SingleUnitUpdate;
