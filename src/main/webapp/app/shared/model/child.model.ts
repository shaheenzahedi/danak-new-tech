import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IProgress } from 'app/shared/model/progress.model';
import { ICentre } from 'app/shared/model/centre.model';
import { IDevice } from 'app/shared/model/device.model';
import { IFacilitator } from 'app/shared/model/facilitator.model';

export interface IChild {
  id?: string;
  createTimeStamp?: string | null;
  user?: IUser | null;
  progresses?: IProgress[] | null;
  centre?: ICentre | null;
  device?: IDevice | null;
  facilitator?: IFacilitator | null;
}

export const defaultValue: Readonly<IChild> = {};
