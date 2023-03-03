import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProgress } from 'app/shared/model/progress.model';
import { getEntities } from './progress.reducer';

export const Progress = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const progressList = useAppSelector(state => state.progress.entities);
  const loading = useAppSelector(state => state.progress.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="progress-heading" data-cy="ProgressHeading">
        Progresses
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/progress/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Progress
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {progressList && progressList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Create Time Stamp</th>
                <th>Spent Time</th>
                <th>Child</th>
                <th>Created By Device</th>
                <th>Single Unit</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {progressList.map((progress, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/progress/${progress.id}`} color="link" size="sm">
                      {progress.id}
                    </Button>
                  </td>
                  <td>
                    {progress.createTimeStamp ? <TextFormat type="date" value={progress.createTimeStamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{progress.spentTime}</td>
                  <td>{progress.child ? <Link to={`/child/${progress.child.id}`}>{progress.child.id}</Link> : ''}</td>
                  <td>
                    {progress.createdByDevice ? (
                      <Link to={`/device/${progress.createdByDevice.id}`}>{progress.createdByDevice.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{progress.singleUnit ? <Link to={`/single-unit/${progress.singleUnit.id}`}>{progress.singleUnit.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/progress/${progress.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/progress/${progress.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/progress/${progress.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Progresses found</div>
        )}
      </div>
    </div>
  );
};

export default Progress;
