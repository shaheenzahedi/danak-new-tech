import dayjs from 'dayjs';
import { ISingleUnit } from 'app/shared/model/single-unit.model';
import { UnitListType } from 'app/shared/model/enumerations/unit-list-type.model';
import { PresenterName } from 'app/shared/model/enumerations/presenter-name.model';

export interface IUnitList {
  id?: string;
  createTimeStamp?: string | null;
  num?: number | null;
  displayName?: string | null;
  type?: UnitListType | null;
  presenter?: PresenterName | null;
  singleUnits?: ISingleUnit[] | null;
}

export const defaultValue: Readonly<IUnitList> = {};
