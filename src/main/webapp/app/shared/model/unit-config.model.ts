import { ISingleUnit } from 'app/shared/model/single-unit.model';

export interface IUnitConfig {
  id?: string;
  name?: string | null;
  displayName?: string | null;
  singleUnits?: ISingleUnit[] | null;
}

export const defaultValue: Readonly<IUnitConfig> = {};
