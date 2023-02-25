import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Facilitator from './facilitator';
import FacilitatorDetail from './facilitator-detail';
import FacilitatorUpdate from './facilitator-update';
import FacilitatorDeleteDialog from './facilitator-delete-dialog';

const FacilitatorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Facilitator />} />
    <Route path="new" element={<FacilitatorUpdate />} />
    <Route path=":id">
      <Route index element={<FacilitatorDetail />} />
      <Route path="edit" element={<FacilitatorUpdate />} />
      <Route path="delete" element={<FacilitatorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FacilitatorRoutes;
