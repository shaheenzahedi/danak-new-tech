import child from 'app/entities/child/child.reducer';
import device from 'app/entities/device/device.reducer';
import facilitator from 'app/entities/facilitator/facilitator.reducer';
import centre from 'app/entities/centre/centre.reducer';
import facilitatorCentreAssociation from 'app/entities/facilitator-centre-association/facilitator-centre-association.reducer';
import province from 'app/entities/province/province.reducer';
import city from 'app/entities/city/city.reducer';
import country from 'app/entities/country/country.reducer';
import progress from 'app/entities/progress/progress.reducer';
import unitList from 'app/entities/unit-list/unit-list.reducer';
import singleUnit from 'app/entities/single-unit/single-unit.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  child,
  device,
  facilitator,
  centre,
  facilitatorCentreAssociation,
  province,
  city,
  country,
  progress,
  unitList,
  singleUnit,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
