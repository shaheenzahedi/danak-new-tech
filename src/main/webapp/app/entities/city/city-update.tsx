import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProvince } from 'app/shared/model/province.model';
import { getEntities as getProvinces } from 'app/entities/province/province.reducer';
import { ICity } from 'app/shared/model/city.model';
import { getEntity, updateEntity, createEntity, reset } from './city.reducer';

export const CityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const provinces = useAppSelector(state => state.province.entities);
  const cityEntity = useAppSelector(state => state.city.entity);
  const loading = useAppSelector(state => state.city.loading);
  const updating = useAppSelector(state => state.city.updating);
  const updateSuccess = useAppSelector(state => state.city.updateSuccess);

  const handleClose = () => {
    navigate('/city');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProvinces({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);

    const entity = {
      ...cityEntity,
      ...values,
      province: provinces.find(it => it.id.toString() === values.province.toString()),
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
          ...cityEntity,
          createTimeStamp: convertDateTimeFromServer(cityEntity.createTimeStamp),
          province: cityEntity?.province?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakBackendApp.city.home.createOrEditLabel" data-cy="CityCreateUpdateHeading">
            Create or edit a City
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="city-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Create Time Stamp"
                id="city-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Name" id="city-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Is Village" id="city-isVillage" name="isVillage" data-cy="isVillage" check type="checkbox" />
              <ValidatedField id="city-province" name="province" data-cy="province" label="Province" type="select">
                <option value="" key="0" />
                {provinces
                  ? provinces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/city" replace color="info">
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

export default CityUpdate;
