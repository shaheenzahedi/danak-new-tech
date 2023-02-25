import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UnitList from './unit-list';
import UnitListDetail from './unit-list-detail';
import UnitListUpdate from './unit-list-update';
import UnitListDeleteDialog from './unit-list-delete-dialog';

const UnitListRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UnitList />} />
    <Route path="new" element={<UnitListUpdate />} />
    <Route path=":id">
      <Route index element={<UnitListDetail />} />
      <Route path="edit" element={<UnitListUpdate />} />
      <Route path="delete" element={<UnitListDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UnitListRoutes;
