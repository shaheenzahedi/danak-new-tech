import dayjs from 'dayjs';
import { ISingleUnit } from 'app/shared/model/single-unit.model';
import { UnitListType } from 'app/shared/model/enumerations/unit-list-type.model';

export interface IUnitList {
  id?: string;
  createTimeStamp?: string | null;
  num?: number | null;
  nickName?: string | null;
  type?: UnitListType | null;
  singleUnits?: ISingleUnit[] | null;
}

export const defaultValue: Readonly<IUnitList> = {};
