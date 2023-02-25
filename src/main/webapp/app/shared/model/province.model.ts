import dayjs from 'dayjs';
import { ICity } from 'app/shared/model/city.model';
import { ICountry } from 'app/shared/model/country.model';

export interface IProvince {
  id?: string;
  createTimeStamp?: string | null;
  name?: string | null;
  cities?: ICity[] | null;
  country?: ICountry | null;
}

export const defaultValue: Readonly<IProvince> = {};
