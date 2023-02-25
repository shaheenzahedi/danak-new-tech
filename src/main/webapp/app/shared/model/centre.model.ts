import dayjs from 'dayjs';
import { IChild } from 'app/shared/model/child.model';
import { ICity } from 'app/shared/model/city.model';
import { IFacilitatorCentreAssociation } from 'app/shared/model/facilitator-centre-association.model';

export interface ICentre {
  id?: string;
  createTimeStamp?: string | null;
  name?: string | null;
  children?: IChild[] | null;
  city?: ICity | null;
  facilitators?: IFacilitatorCentreAssociation[] | null;
}

export const defaultValue: Readonly<ICentre> = {};
