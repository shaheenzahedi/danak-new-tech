import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFacilitatorCentreAssociation } from 'app/shared/model/facilitator-centre-association.model';
import { getEntities } from './facilitator-centre-association.reducer';

export const FacilitatorCentreAssociation = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const facilitatorCentreAssociationList = useAppSelector(state => state.facilitatorCentreAssociation.entities);
  const loading = useAppSelector(state => state.facilitatorCentreAssociation.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="facilitator-centre-association-heading" data-cy="FacilitatorCentreAssociationHeading">
        Facilitator Centre Associations
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link
            to="/facilitator-centre-association/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Facilitator Centre Association
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {facilitatorCentreAssociationList && facilitatorCentreAssociationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Create Time Stamp</th>
                <th>Join Date</th>
                <th>Facilitator</th>
                <th>Centre</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {facilitatorCentreAssociationList.map((facilitatorCentreAssociation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/facilitator-centre-association/${facilitatorCentreAssociation.id}`} color="link" size="sm">
                      {facilitatorCentreAssociation.id}
                    </Button>
                  </td>
                  <td>
                    {facilitatorCentreAssociation.createTimeStamp ? (
                      <TextFormat type="date" value={facilitatorCentreAssociation.createTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {facilitatorCentreAssociation.joinDate ? (
                      <TextFormat type="date" value={facilitatorCentreAssociation.joinDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {facilitatorCentreAssociation.facilitator ? (
                      <Link to={`/facilitator/${facilitatorCentreAssociation.facilitator.id}`}>
                        {facilitatorCentreAssociation.facilitator.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {facilitatorCentreAssociation.centre ? (
                      <Link to={`/centre/${facilitatorCentreAssociation.centre.id}`}>{facilitatorCentreAssociation.centre.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/facilitator-centre-association/${facilitatorCentreAssociation.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/facilitator-centre-association/${facilitatorCentreAssociation.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/facilitator-centre-association/${facilitatorCentreAssociation.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Facilitator Centre Associations found</div>
        )}
      </div>
    </div>
  );
};

export default FacilitatorCentreAssociation;
