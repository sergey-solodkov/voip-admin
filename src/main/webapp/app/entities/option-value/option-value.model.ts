import { ISetting } from 'app/entities/setting/setting.model';
import { IOption } from 'app/entities/option/option.model';

export interface IOptionValue {
  id: number;
  value?: string | null;
  settings?: Pick<ISetting, 'id'>[] | null;
  option?: Pick<IOption, 'id'> | null;
}

export type NewOptionValue = Omit<IOptionValue, 'id'> & { id: null };
