import { IVendor } from 'app/entities/vendor/vendor.model';
import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { OptionValueType } from 'app/entities/enumerations/option-value-type.model';
import { IOptionValue } from '../option-value/option-value.model';

export interface IOption {
  id: number;
  code?: string | null;
  descr?: string | null;
  valueType?: keyof typeof OptionValueType | null;
  multiple?: boolean | null;
  vendors?: Pick<IVendor, 'id' | 'name'>[] | null;
  models?: Pick<IDeviceModel, 'id' | 'name'>[] | null;
  possibleValues?: Pick<IOptionValue, 'id' | 'value'>[] | null;
}

export type NewOption = Omit<IOption, 'id'> & { id: null };
