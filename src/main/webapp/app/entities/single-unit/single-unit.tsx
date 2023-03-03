import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISingleUnit } from 'app/shared/model/single-unit.model';
import { getEntities } from './single-unit.reducer';

export const SingleUnit = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const singleUnitList = useAppSelector(state => state.singleUnit.entities);
  const loading = useAppSelector(state => state.singleUnit.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="single-unit-heading" data-cy="SingleUnitHeading">
        Single Units
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/single-unit/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Single Unit
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {singleUnitList && singleUnitList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Create Time Stamp</th>
                <th>Global Num</th>
                <th>Icon</th>
                <th>Target</th>
                <th>Params</th>
                <th>Words</th>
                <th>Unit List</th>
                <th>Config</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {singleUnitList.map((singleUnit, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/single-unit/${singleUnit.id}`} color="link" size="sm">
                      {singleUnit.id}
                    </Button>
                  </td>
                  <td>
                    {singleUnit.createTimeStamp ? (
                      <TextFormat type="date" value={singleUnit.createTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{singleUnit.globalNum}</td>
                  <td>{singleUnit.icon}</td>
                  <td>{singleUnit.target}</td>
                  <td>{singleUnit.params}</td>
                  <td>{singleUnit.words}</td>
                  <td>{singleUnit.unitList ? <Link to={`/unit-list/${singleUnit.unitList.id}`}>{singleUnit.unitList.id}</Link> : ''}</td>
                  <td>{singleUnit.config ? <Link to={`/unit-config/${singleUnit.config.id}`}>{singleUnit.config.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/single-unit/${singleUnit.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/single-unit/${singleUnit.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/single-unit/${singleUnit.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Single Units found</div>
        )}
      </div>
    </div>
  );
};

export default SingleUnit;
