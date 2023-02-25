import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './facilitator-centre-association.reducer';

export const FacilitatorCentreAssociationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const facilitatorCentreAssociationEntity = useAppSelector(state => state.facilitatorCentreAssociation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="facilitatorCentreAssociationDetailsHeading">Facilitator Centre Association</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{facilitatorCentreAssociationEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">Create Time Stamp</span>
          </dt>
          <dd>
            {facilitatorCentreAssociationEntity.createTimeStamp ? (
              <TextFormat value={facilitatorCentreAssociationEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="joinDate">Join Date</span>
          </dt>
          <dd>
            {facilitatorCentreAssociationEntity.joinDate ? (
              <TextFormat value={facilitatorCentreAssociationEntity.joinDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Facilitator</dt>
          <dd>{facilitatorCentreAssociationEntity.facilitator ? facilitatorCentreAssociationEntity.facilitator.id : ''}</dd>
          <dt>Centre</dt>
          <dd>{facilitatorCentreAssociationEntity.centre ? facilitatorCentreAssociationEntity.centre.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/facilitator-centre-association" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facilitator-centre-association/${facilitatorCentreAssociationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FacilitatorCentreAssociationDetail;
