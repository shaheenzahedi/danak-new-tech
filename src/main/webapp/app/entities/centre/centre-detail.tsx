import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './centre.reducer';

export const CentreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const centreEntity = useAppSelector(state => state.centre.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="centreDetailsHeading">Centre</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{centreEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {centreEntity.createTimeStamp ? <TextFormat value={centreEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{centreEntity.name}</dd>
          <dt>City</dt>
          <dd>{centreEntity.city ? centreEntity.city.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/centre" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/centre/${centreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CentreDetail;
