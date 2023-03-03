import dayjs from 'dayjs';
import { IChild } from 'app/shared/model/child.model';
import { IDevice } from 'app/shared/model/device.model';
import { ISingleUnit } from 'app/shared/model/single-unit.model';

export interface IProgress {
  id?: string;
  createTimeStamp?: string | null;
  spentTime?: number | null;
  child?: IChild | null;
  createdByDevice?: IDevice | null;
  singleUnit?: ISingleUnit | null;
}

export const defaultValue: Readonly<IProgress> = {};
