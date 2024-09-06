import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOtherDeviceType, NewOtherDeviceType } from '../other-device-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOtherDeviceType for edit and NewOtherDeviceTypeFormGroupInput for create.
 */
type OtherDeviceTypeFormGroupInput = IOtherDeviceType | PartialWithRequiredKeyOf<NewOtherDeviceType>;

type OtherDeviceTypeFormDefaults = Pick<NewOtherDeviceType, 'id'>;

type OtherDeviceTypeFormGroupContent = {
  id: FormControl<IOtherDeviceType['id'] | NewOtherDeviceType['id']>;
  name: FormControl<IOtherDeviceType['name']>;
  description: FormControl<IOtherDeviceType['description']>;
};

export type OtherDeviceTypeFormGroup = FormGroup<OtherDeviceTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OtherDeviceTypeFormService {
  createOtherDeviceTypeFormGroup(otherDeviceType: OtherDeviceTypeFormGroupInput = { id: null }): OtherDeviceTypeFormGroup {
    const otherDeviceTypeRawValue = {
      ...this.getFormDefaults(),
      ...otherDeviceType,
    };
    return new FormGroup<OtherDeviceTypeFormGroupContent>({
      id: new FormControl(
        { value: otherDeviceTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(otherDeviceTypeRawValue.name),
      description: new FormControl(otherDeviceTypeRawValue.description),
    });
  }

  getOtherDeviceType(form: OtherDeviceTypeFormGroup): IOtherDeviceType | NewOtherDeviceType {
    return form.getRawValue() as IOtherDeviceType | NewOtherDeviceType;
  }

  resetForm(form: OtherDeviceTypeFormGroup, otherDeviceType: OtherDeviceTypeFormGroupInput): void {
    const otherDeviceTypeRawValue = { ...this.getFormDefaults(), ...otherDeviceType };
    form.reset(
      {
        ...otherDeviceTypeRawValue,
        id: { value: otherDeviceTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OtherDeviceTypeFormDefaults {
    return {
      id: null,
    };
  }
}
