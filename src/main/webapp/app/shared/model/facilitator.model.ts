import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IChild } from 'app/shared/model/child.model';
import { IFacilitatorCentreAssociation } from 'app/shared/model/facilitator-centre-association.model';

export interface IFacilitator {
  id?: string;
  createTimeStamp?: string | null;
  user?: IUser | null;
  children?: IChild[] | null;
  centres?: IFacilitatorCentreAssociation[] | null;
}

export const defaultValue: Readonly<IFacilitator> = {};
