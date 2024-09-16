import { IOption } from 'app/entities/option/option.model';
import { IOptionValue } from 'app/entities/option-value/option-value.model';
import { IDevice } from 'app/entities/device/device.model';

export interface ISetting {
  id: number;
  textValue?: string | null;
  option?: Pick<IOption, 'id' | 'code' | 'multiple'> | null;
  selectedValues?: Pick<IOptionValue, 'id'>[] | null;
  device?: Pick<IDevice, 'id'> | null;
}

export type NewSetting = Omit<ISetting, 'id'> & { id: null };
