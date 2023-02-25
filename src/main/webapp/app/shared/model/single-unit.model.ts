import dayjs from 'dayjs';
import { IProgress } from 'app/shared/model/progress.model';
import { IUnitList } from 'app/shared/model/unit-list.model';

export interface ISingleUnit {
  id?: string;
  createTimeStamp?: string | null;
  globalNum?: string | null;
  progresses?: IProgress[] | null;
  unitList?: IUnitList | null;
}

export const defaultValue: Readonly<ISingleUnit> = {};
