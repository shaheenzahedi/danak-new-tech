import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDevice } from 'app/shared/model/device.model';
import { getEntities } from './device.reducer';

export const Device = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const deviceList = useAppSelector(state => state.device.entities);
  const loading = useAppSelector(state => state.device.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="device-heading" data-cy="DeviceHeading">
        Devices
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/device/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Device
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {deviceList && deviceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Create Time Stamp</th>
                <th>Universal Id</th>
                <th>Global Num</th>
                <th>Model</th>
                <th>Year Built</th>
                <th>Android Id</th>
                <th>City</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {deviceList.map((device, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/device/${device.id}`} color="link" size="sm">
                      {device.id}
                    </Button>
                  </td>
                  <td>
                    {device.createTimeStamp ? <TextFormat type="date" value={device.createTimeStamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{device.universalId}</td>
                  <td>{device.globalNum}</td>
                  <td>{device.model}</td>
                  <td>{device.yearBuilt}</td>
                  <td>{device.androidId}</td>
                  <td>{device.city ? <Link to={`/city/${device.city.id}`}>{device.city.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/device/${device.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/device/${device.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/device/${device.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Devices found</div>
        )}
      </div>
    </div>
  );
};

export default Device;
