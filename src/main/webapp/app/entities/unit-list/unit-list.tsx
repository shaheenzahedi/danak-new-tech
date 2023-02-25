import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUnitList } from 'app/shared/model/unit-list.model';
import { getEntities } from './unit-list.reducer';

export const UnitList = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const unitListList = useAppSelector(state => state.unitList.entities);
  const loading = useAppSelector(state => state.unitList.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="unit-list-heading" data-cy="UnitListHeading">
        Unit Lists
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/unit-list/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Unit List
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {unitListList && unitListList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Create Time Stamp</th>
                <th>Num</th>
                <th>Nick Name</th>
                <th>Type</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {unitListList.map((unitList, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/unit-list/${unitList.id}`} color="link" size="sm">
                      {unitList.id}
                    </Button>
                  </td>
                  <td>
                    {unitList.createTimeStamp ? <TextFormat type="date" value={unitList.createTimeStamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{unitList.num}</td>
                  <td>{unitList.nickName}</td>
                  <td>{unitList.type}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/unit-list/${unitList.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/unit-list/${unitList.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/unit-list/${unitList.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Unit Lists found</div>
        )}
      </div>
    </div>
  );
};

export default UnitList;
