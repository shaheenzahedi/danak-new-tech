import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UnitConfig from './unit-config';
import UnitConfigDetail from './unit-config-detail';
import UnitConfigUpdate from './unit-config-update';
import UnitConfigDeleteDialog from './unit-config-delete-dialog';

const UnitConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UnitConfig />} />
    <Route path="new" element={<UnitConfigUpdate />} />
    <Route path=":id">
      <Route index element={<UnitConfigDetail />} />
      <Route path="edit" element={<UnitConfigUpdate />} />
      <Route path="delete" element={<UnitConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UnitConfigRoutes;
