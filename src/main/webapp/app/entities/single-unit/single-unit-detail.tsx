import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-unit.reducer';

export const SingleUnitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singleUnitEntity = useAppSelector(state => state.singleUnit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singleUnitDetailsHeading">Single Unit</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{singleUnitEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {singleUnitEntity.createTimeStamp ? (
              <TextFormat value={singleUnitEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="globalNum">Global Num</span>
          </dt>
          <dd>{singleUnitEntity.globalNum}</dd>
          <dt>
            <span id="icon">Icon</span>
          </dt>
          <dd>{singleUnitEntity.icon}</dd>
          <dt>
            <span id="target">Target</span>
          </dt>
          <dd>{singleUnitEntity.target}</dd>
          <dt>
            <span id="params">Params</span>
          </dt>
          <dd>{singleUnitEntity.params}</dd>
          <dt>
            <span id="words">Words</span>
          </dt>
          <dd>{singleUnitEntity.words}</dd>
          <dt>Unit List</dt>
          <dd>{singleUnitEntity.unitList ? singleUnitEntity.unitList.id : ''}</dd>
          <dt>Config</dt>
          <dd>{singleUnitEntity.config ? singleUnitEntity.config.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/single-unit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-unit/${singleUnitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SingleUnitDetail;
