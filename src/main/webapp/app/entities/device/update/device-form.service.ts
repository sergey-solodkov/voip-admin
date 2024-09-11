import { Injectable } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { IDevice, NewDevice } from '../device.model';
import { VoipAccountFormGroup } from 'app/entities/voip-account/update/voip-account-form.service';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDevice for edit and NewDeviceFormGroupInput for create.
 */
type DeviceFormGroupInput = IDevice | PartialWithRequiredKeyOf<NewDevice>;

type DeviceFormDefaults = Pick<NewDevice, 'id' | 'dhcpEnabled'>;

type DeviceFormGroupContent = {
  id: FormControl<IDevice['id'] | NewDevice['id']>;
  mac: FormControl<IDevice['mac']>;
  inventoryId: FormControl<IDevice['inventoryId']>;
  location: FormControl<IDevice['location']>;
  hostname: FormControl<IDevice['hostname']>;
  webAccessLogin: FormControl<IDevice['webAccessLogin']>;
  webAccessPasswordHash: FormControl<IDevice['webAccessPasswordHash']>;
  dhcpEnabled: FormControl<IDevice['dhcpEnabled']>;
  ipAddress: FormControl<IDevice['ipAddress']>;
  subnetMask: FormControl<IDevice['subnetMask']>;
  defaultGw: FormControl<IDevice['defaultGw']>;
  dns1: FormControl<IDevice['dns1']>;
  dns2: FormControl<IDevice['dns2']>;
  provisioningMode: FormControl<IDevice['provisioningMode']>;
  provisioningUrl: FormControl<IDevice['provisioningUrl']>;
  ntp: FormControl<IDevice['ntp']>;
  configPath: FormControl<IDevice['configPath']>;
  notes: FormControl<IDevice['notes']>;
  model: FormControl<IDevice['model']>;
  owner: FormControl<IDevice['owner']>;
  parent: FormControl<IDevice['parent']>;
  voipAccounts: FormArray<VoipAccountFormGroup>;
};

export type DeviceFormGroup = FormGroup<DeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceFormService {
  createDeviceFormGroup(device: DeviceFormGroupInput = { id: null }): DeviceFormGroup {
    const deviceRawValue = {
      ...this.getFormDefaults(),
      ...device,
    };
    return new FormGroup<DeviceFormGroupContent>({
      id: new FormControl(
        { value: deviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mac: new FormControl(deviceRawValue.mac, {
        validators: [Validators.required, Validators.pattern('^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$')],
      }),
      inventoryId: new FormControl(deviceRawValue.inventoryId),
      location: new FormControl(deviceRawValue.location),
      hostname: new FormControl(deviceRawValue.hostname),
      webAccessLogin: new FormControl(deviceRawValue.webAccessLogin),
      webAccessPasswordHash: new FormControl(deviceRawValue.webAccessPasswordHash),
      dhcpEnabled: new FormControl(deviceRawValue.dhcpEnabled),
      ipAddress: new FormControl(deviceRawValue.ipAddress),
      subnetMask: new FormControl(deviceRawValue.subnetMask),
      defaultGw: new FormControl(deviceRawValue.defaultGw),
      dns1: new FormControl(deviceRawValue.dns1),
      dns2: new FormControl(deviceRawValue.dns2),
      provisioningMode: new FormControl(deviceRawValue.provisioningMode),
      provisioningUrl: new FormControl(deviceRawValue.provisioningUrl),
      ntp: new FormControl(deviceRawValue.ntp),
      configPath: new FormControl(deviceRawValue.configPath),
      notes: new FormControl(deviceRawValue.notes),
      model: new FormControl(deviceRawValue.model),
      owner: new FormControl(deviceRawValue.owner),
      parent: new FormControl(deviceRawValue.parent),
      voipAccounts: new FormArray<VoipAccountFormGroup>([]),
    });
  }

  getDevice(form: DeviceFormGroup): IDevice | NewDevice {
    return form.getRawValue() as IDevice | NewDevice;
  }

  resetForm(form: DeviceFormGroup, device: DeviceFormGroupInput): void {
    const deviceRawValue = { ...this.getFormDefaults(), ...device };
    form.reset(
      {
        ...deviceRawValue,
        id: { value: deviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeviceFormDefaults {
    return {
      id: null,
      dhcpEnabled: false,
    };
  }
}
