import dayjs from 'dayjs';
import { IFacilitator } from 'app/shared/model/facilitator.model';
import { ICentre } from 'app/shared/model/centre.model';

export interface IFacilitatorCentreAssociation {
  id?: string;
  createTimeStamp?: string | null;
  joinDate?: string | null;
  facilitator?: IFacilitator | null;
  centre?: ICentre | null;
}

export const defaultValue: Readonly<IFacilitatorCentreAssociation> = {};
