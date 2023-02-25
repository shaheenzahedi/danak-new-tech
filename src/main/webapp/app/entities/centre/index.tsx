import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Centre from './centre';
import CentreDetail from './centre-detail';
import CentreUpdate from './centre-update';
import CentreDeleteDialog from './centre-delete-dialog';

const CentreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Centre />} />
    <Route path="new" element={<CentreUpdate />} />
    <Route path=":id">
      <Route index element={<CentreDetail />} />
      <Route path="edit" element={<CentreUpdate />} />
      <Route path="delete" element={<CentreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CentreRoutes;
