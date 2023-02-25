import dayjs from 'dayjs';
import { IChild } from 'app/shared/model/child.model';
import { ICity } from 'app/shared/model/city.model';

export interface IDevice {
  id?: string;
  createTimeStamp?: string | null;
  universalId?: string | null;
  globalNum?: string | null;
  model?: string | null;
  yearBuilt?: string | null;
  androidId?: string | null;
  children?: IChild[] | null;
  city?: ICity | null;
}

export const defaultValue: Readonly<IDevice> = {};
