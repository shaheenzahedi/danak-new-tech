import dayjs from 'dayjs';
import { ICentre } from 'app/shared/model/centre.model';
import { IDevice } from 'app/shared/model/device.model';
import { IProvince } from 'app/shared/model/province.model';

export interface ICity {
  id?: string;
  createTimeStamp?: string | null;
  name?: string | null;
  isVillage?: boolean | null;
  centres?: ICentre[] | null;
  devices?: IDevice[] | null;
  province?: IProvince | null;
}

export const defaultValue: Readonly<ICity> = {
  isVillage: false,
};
