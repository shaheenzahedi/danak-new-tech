import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Child from './child';
import Device from './device';
import Facilitator from './facilitator';
import Centre from './centre';
import FacilitatorCentreAssociation from './facilitator-centre-association';
import Province from './province';
import City from './city';
import Country from './country';
import Progress from './progress';
import UnitList from './unit-list';
import SingleUnit from './single-unit';
import UnitConfig from './unit-config';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="child/*" element={<Child />} />
        <Route path="device/*" element={<Device />} />
        <Route path="facilitator/*" element={<Facilitator />} />
        <Route path="centre/*" element={<Centre />} />
        <Route path="facilitator-centre-association/*" element={<FacilitatorCentreAssociation />} />
        <Route path="province/*" element={<Province />} />
        <Route path="city/*" element={<City />} />
        <Route path="country/*" element={<Country />} />
        <Route path="progress/*" element={<Progress />} />
        <Route path="unit-list/*" element={<UnitList />} />
        <Route path="single-unit/*" element={<SingleUnit />} />
        <Route path="unit-config/*" element={<UnitConfig />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
