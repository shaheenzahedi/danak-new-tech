import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './province.reducer';

export const ProvinceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const provinceEntity = useAppSelector(state => state.province.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="provinceDetailsHeading">Province</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{provinceEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {provinceEntity.createTimeStamp ? (
              <TextFormat value={provinceEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{provinceEntity.name}</dd>
          <dt>Country</dt>
          <dd>{provinceEntity.country ? provinceEntity.country.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/province" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/province/${provinceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProvinceDetail;
