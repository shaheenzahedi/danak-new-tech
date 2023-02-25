import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChild } from 'app/shared/model/child.model';
import { getEntities } from './child.reducer';

export const Child = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const childList = useAppSelector(state => state.child.entities);
  const loading = useAppSelector(state => state.child.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="child-heading" data-cy="ChildHeading">
        Children
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/child/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Child
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {childList && childList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Create Time Stamp</th>
                <th>User</th>
                <th>Centre</th>
                <th>Device</th>
                <th>Facilitator</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {childList.map((child, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/child/${child.id}`} color="link" size="sm">
                      {child.id}
                    </Button>
                  </td>
                  <td>
                    {child.createTimeStamp ? <TextFormat type="date" value={child.createTimeStamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{child.user ? child.user.id : ''}</td>
                  <td>{child.centre ? <Link to={`/centre/${child.centre.id}`}>{child.centre.id}</Link> : ''}</td>
                  <td>{child.device ? <Link to={`/device/${child.device.id}`}>{child.device.id}</Link> : ''}</td>
                  <td>{child.facilitator ? <Link to={`/facilitator/${child.facilitator.id}`}>{child.facilitator.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/child/${child.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/child/${child.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/child/${child.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Children found</div>
        )}
      </div>
    </div>
  );
};

export default Child;
