import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SingleUnit from './single-unit';
import SingleUnitDetail from './single-unit-detail';
import SingleUnitUpdate from './single-unit-update';
import SingleUnitDeleteDialog from './single-unit-delete-dialog';

const SingleUnitRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SingleUnit />} />
    <Route path="new" element={<SingleUnitUpdate />} />
    <Route path=":id">
      <Route index element={<SingleUnitDetail />} />
      <Route path="edit" element={<SingleUnitUpdate />} />
      <Route path="delete" element={<SingleUnitDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SingleUnitRoutes;
