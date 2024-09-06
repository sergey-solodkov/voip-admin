import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVendor, NewVendor } from '../vendor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVendor for edit and NewVendorFormGroupInput for create.
 */
type VendorFormGroupInput = IVendor | PartialWithRequiredKeyOf<NewVendor>;

type VendorFormDefaults = Pick<NewVendor, 'id' | 'options'>;

type VendorFormGroupContent = {
  id: FormControl<IVendor['id'] | NewVendor['id']>;
  name: FormControl<IVendor['name']>;
  options: FormControl<IVendor['options']>;
};

export type VendorFormGroup = FormGroup<VendorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VendorFormService {
  createVendorFormGroup(vendor: VendorFormGroupInput = { id: null }): VendorFormGroup {
    const vendorRawValue = {
      ...this.getFormDefaults(),
      ...vendor,
    };
    return new FormGroup<VendorFormGroupContent>({
      id: new FormControl(
        { value: vendorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(vendorRawValue.name),
      options: new FormControl(vendorRawValue.options ?? []),
    });
  }

  getVendor(form: VendorFormGroup): IVendor | NewVendor {
    return form.getRawValue() as IVendor | NewVendor;
  }

  resetForm(form: VendorFormGroup, vendor: VendorFormGroupInput): void {
    const vendorRawValue = { ...this.getFormDefaults(), ...vendor };
    form.reset(
      {
        ...vendorRawValue,
        id: { value: vendorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VendorFormDefaults {
    return {
      id: null,
      options: [],
    };
  }
}
