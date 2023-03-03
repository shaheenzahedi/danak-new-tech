import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './unit-config.reducer';

export const UnitConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const unitConfigEntity = useAppSelector(state => state.unitConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unitConfigDetailsHeading">Unit Config</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{unitConfigEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{unitConfigEntity.name}</dd>
          <dt>
            <span id="displayName">Display Name</span>
          </dt>
          <dd>{unitConfigEntity.displayName}</dd>
        </dl>
        <Button tag={Link} to="/unit-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unit-config/${unitConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnitConfigDetail;
