import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './unit-list.reducer';

export const UnitListDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const unitListEntity = useAppSelector(state => state.unitList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unitListDetailsHeading">Unit List</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{unitListEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {unitListEntity.createTimeStamp ? (
              <TextFormat value={unitListEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="num">Num</span>
          </dt>
          <dd>{unitListEntity.num}</dd>
          <dt>
            <span id="nickName">Nick Name</span>
          </dt>
          <dd>{unitListEntity.nickName}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{unitListEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/unit-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unit-list/${unitListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnitListDetail;
