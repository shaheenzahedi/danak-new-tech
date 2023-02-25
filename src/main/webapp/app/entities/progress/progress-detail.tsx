import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './progress.reducer';

export const ProgressDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const progressEntity = useAppSelector(state => state.progress.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="progressDetailsHeading">Progress</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{progressEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {progressEntity.createTimeStamp ? (
              <TextFormat value={progressEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="spentTime">Spent Time</span>
          </dt>
          <dd>{progressEntity.spentTime}</dd>
          <dt>Child</dt>
          <dd>{progressEntity.child ? progressEntity.child.id : ''}</dd>
          <dt>Single Unit</dt>
          <dd>{progressEntity.singleUnit ? progressEntity.singleUnit.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/progress" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/progress/${progressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProgressDetail;
