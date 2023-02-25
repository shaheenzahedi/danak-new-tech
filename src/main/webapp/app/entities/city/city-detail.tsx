import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './city.reducer';

export const CityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cityEntity = useAppSelector(state => state.city.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cityDetailsHeading">City</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{cityEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {cityEntity.createTimeStamp ? <TextFormat value={cityEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{cityEntity.name}</dd>
          <dt>
            <span id="isVillage">Is Village</span>
          </dt>
          <dd>{cityEntity.isVillage ? 'true' : 'false'}</dd>
          <dt>Province</dt>
          <dd>{cityEntity.province ? cityEntity.province.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/city" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/city/${cityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CityDetail;
