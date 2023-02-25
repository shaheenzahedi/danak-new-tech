import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FacilitatorCentreAssociation from './facilitator-centre-association';
import FacilitatorCentreAssociationDetail from './facilitator-centre-association-detail';
import FacilitatorCentreAssociationUpdate from './facilitator-centre-association-update';
import FacilitatorCentreAssociationDeleteDialog from './facilitator-centre-association-delete-dialog';

const FacilitatorCentreAssociationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FacilitatorCentreAssociation />} />
    <Route path="new" element={<FacilitatorCentreAssociationUpdate />} />
    <Route path=":id">
      <Route index element={<FacilitatorCentreAssociationDetail />} />
      <Route path="edit" element={<FacilitatorCentreAssociationUpdate />} />
      <Route path="delete" element={<FacilitatorCentreAssociationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FacilitatorCentreAssociationRoutes;
