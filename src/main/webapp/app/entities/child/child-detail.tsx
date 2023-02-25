import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './child.reducer';

export const ChildDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const childEntity = useAppSelector(state => state.child.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="childDetailsHeading">Child</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{childEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {childEntity.createTimeStamp ? <TextFormat value={childEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{childEntity.user ? childEntity.user.id : ''}</dd>
          <dt>Centre</dt>
          <dd>{childEntity.centre ? childEntity.centre.id : ''}</dd>
          <dt>Device</dt>
          <dd>{childEntity.device ? childEntity.device.id : ''}</dd>
          <dt>Facilitator</dt>
          <dd>{childEntity.facilitator ? childEntity.facilitator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/child" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/child/${childEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChildDetail;
