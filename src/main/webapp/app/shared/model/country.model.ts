import dayjs from 'dayjs';
import { IProvince } from 'app/shared/model/province.model';

export interface ICountry {
  id?: string;
  createTimeStamp?: string | null;
  name?: string | null;
  provinces?: IProvince[] | null;
}

export const defaultValue: Readonly<ICountry> = {};
