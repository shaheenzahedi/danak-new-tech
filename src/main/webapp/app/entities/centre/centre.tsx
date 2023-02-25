import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICentre } from 'app/shared/model/centre.model';
import { getEntities } from './centre.reducer';

export const Centre = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const centreList = useAppSelector(state => state.centre.entities);
  const loading = useAppSelector(state => state.centre.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="centre-heading" data-cy="CentreHeading">
        Centres
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/centre/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Centre
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {centreList && centreList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Create Time Stamp</th>
                <th>Name</th>
                <th>City</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {centreList.map((centre, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/centre/${centre.id}`} color="link" size="sm">
                      {centre.id}
                    </Button>
                  </td>
                  <td>
                    {centre.createTimeStamp ? <TextFormat type="date" value={centre.createTimeStamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{centre.name}</td>
                  <td>{centre.city ? <Link to={`/city/${centre.city.id}`}>{centre.city.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/centre/${centre.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/centre/${centre.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/centre/${centre.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Centres found</div>
        )}
      </div>
    </div>
  );
};

export default Centre;
