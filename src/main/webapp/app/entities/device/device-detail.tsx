import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './device.reducer';

export const DeviceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const deviceEntity = useAppSelector(state => state.device.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deviceDetailsHeading">Device</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{deviceEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {deviceEntity.createTimeStamp ? <TextFormat value={deviceEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="universalId">Universal Id</span>
          </dt>
          <dd>{deviceEntity.universalId}</dd>
          <dt>
            <span id="globalNum">Global Num</span>
          </dt>
          <dd>{deviceEntity.globalNum}</dd>
          <dt>
            <span id="model">Model</span>
          </dt>
          <dd>{deviceEntity.model}</dd>
          <dt>
            <span id="yearBuilt">Year Built</span>
          </dt>
          <dd>{deviceEntity.yearBuilt}</dd>
          <dt>
            <span id="androidId">Android Id</span>
          </dt>
          <dd>{deviceEntity.androidId}</dd>
          <dt>City</dt>
          <dd>{deviceEntity.city ? deviceEntity.city.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/device" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/device/${deviceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeviceDetail;
