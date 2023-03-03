import dayjs from 'dayjs';
import { IProgress } from 'app/shared/model/progress.model';
import { IUnitList } from 'app/shared/model/unit-list.model';
import { IUnitConfig } from 'app/shared/model/unit-config.model';

export interface ISingleUnit {
  id?: string;
  createTimeStamp?: string | null;
  globalNum?: string | null;
  icon?: string | null;
  target?: string | null;
  params?: string | null;
  words?: string | null;
  progresses?: IProgress[] | null;
  unitList?: IUnitList | null;
  config?: IUnitConfig | null;
}

export const defaultValue: Readonly<ISingleUnit> = {};
