import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDeviceModel, NewDeviceModel } from '../device-model.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeviceModel for edit and NewDeviceModelFormGroupInput for create.
 */
type DeviceModelFormGroupInput = IDeviceModel | PartialWithRequiredKeyOf<NewDeviceModel>;

type DeviceModelFormDefaults = Pick<NewDeviceModel, 'id' | 'configurable' | 'options'>;

type DeviceModelFormGroupContent = {
  id: FormControl<IDeviceModel['id'] | NewDeviceModel['id']>;
  name: FormControl<IDeviceModel['name']>;
  configurable: FormControl<IDeviceModel['configurable']>;
  linesAmount: FormControl<IDeviceModel['linesAmount']>;
  configTemplatePath: FormControl<IDeviceModel['configTemplatePath']>;
  firmwareFilePath: FormControl<IDeviceModel['firmwareFilePath']>;
  deviceType: FormControl<IDeviceModel['deviceType']>;
  otherDeviceType: FormControl<IDeviceModel['otherDeviceType']>;
  vendor: FormControl<IDeviceModel['vendor']>;
  options: FormControl<IDeviceModel['options']>;
};

export type DeviceModelFormGroup = FormGroup<DeviceModelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceModelFormService {
  createDeviceModelFormGroup(deviceModel: DeviceModelFormGroupInput = { id: null }): DeviceModelFormGroup {
    const deviceModelRawValue = {
      ...this.getFormDefaults(),
      ...deviceModel,
    };
    return new FormGroup<DeviceModelFormGroupContent>({
      id: new FormControl(
        { value: deviceModelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(deviceModelRawValue.name, {
        validators: [Validators.required],
      }),
      configurable: new FormControl(deviceModelRawValue.configurable, {
        validators: [Validators.required],
      }),
      linesAmount: new FormControl(deviceModelRawValue.linesAmount),
      configTemplatePath: new FormControl(deviceModelRawValue.configTemplatePath),
      firmwareFilePath: new FormControl(deviceModelRawValue.firmwareFilePath),
      deviceType: new FormControl(deviceModelRawValue.deviceType),
      otherDeviceType: new FormControl(deviceModelRawValue.otherDeviceType),
      vendor: new FormControl(deviceModelRawValue.vendor),
      options: new FormControl(deviceModelRawValue.options ?? []),
    });
  }

  getDeviceModel(form: DeviceModelFormGroup): IDeviceModel | NewDeviceModel {
    return form.getRawValue() as IDeviceModel | NewDeviceModel;
  }

  resetForm(form: DeviceModelFormGroup, deviceModel: DeviceModelFormGroupInput): void {
    const deviceModelRawValue = { ...this.getFormDefaults(), ...deviceModel };
    form.reset(
      {
        ...deviceModelRawValue,
        id: { value: deviceModelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeviceModelFormDefaults {
    return {
      id: null,
      configurable: false,
      options: [],
    };
  }
}
