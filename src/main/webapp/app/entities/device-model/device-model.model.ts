import { IOtherDeviceType } from 'app/entities/other-device-type/other-device-type.model';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { IOption } from 'app/entities/option/option.model';
import { DeviceType } from 'app/entities/enumerations/device-type.model';

export interface IDeviceModel {
  id: number;
  name?: string | null;
  configurable?: boolean | null;
  linesAmount?: number | null;
  configTemplatePath?: string | null;
  firmwareFilePath?: string | null;
  deviceType?: keyof typeof DeviceType | null;
  otherDeviceType?: Pick<IOtherDeviceType, 'id'> | null;
  vendor?: Pick<IVendor, 'id' | 'name'> | null;
  options?: Pick<IOption, 'id' | 'code'>[] | null;
}

export type NewDeviceModel = Omit<IDeviceModel, 'id'> & { id: null };
