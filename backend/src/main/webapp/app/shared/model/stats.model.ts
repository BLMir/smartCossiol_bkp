import { Moment } from 'moment';
import { IDevices } from 'app/shared/model/devices.model';

export interface IStats {
  id?: number;
  temp?: number;
  soil?: number;
  light?: number;
  insertAt?: Moment;
  devices?: IDevices;
}

export const defaultValue: Readonly<IStats> = {};
