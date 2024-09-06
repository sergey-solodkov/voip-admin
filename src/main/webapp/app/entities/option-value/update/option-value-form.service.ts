import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOptionValue, NewOptionValue } from '../option-value.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOptionValue for edit and NewOptionValueFormGroupInput for create.
 */
type OptionValueFormGroupInput = IOptionValue | PartialWithRequiredKeyOf<NewOptionValue>;

type OptionValueFormDefaults = Pick<NewOptionValue, 'id' | 'settings'>;

type OptionValueFormGroupContent = {
  id: FormControl<IOptionValue['id'] | NewOptionValue['id']>;
  value: FormControl<IOptionValue['value']>;
  settings: FormControl<IOptionValue['settings']>;
  option: FormControl<IOptionValue['option']>;
};

export type OptionValueFormGroup = FormGroup<OptionValueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OptionValueFormService {
  createOptionValueFormGroup(optionValue: OptionValueFormGroupInput = { id: null }): OptionValueFormGroup {
    const optionValueRawValue = {
      ...this.getFormDefaults(),
      ...optionValue,
    };
    return new FormGroup<OptionValueFormGroupContent>({
      id: new FormControl(
        { value: optionValueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      value: new FormControl(optionValueRawValue.value),
      settings: new FormControl(optionValueRawValue.settings ?? []),
      option: new FormControl(optionValueRawValue.option),
    });
  }

  getOptionValue(form: OptionValueFormGroup): IOptionValue | NewOptionValue {
    return form.getRawValue() as IOptionValue | NewOptionValue;
  }

  resetForm(form: OptionValueFormGroup, optionValue: OptionValueFormGroupInput): void {
    const optionValueRawValue = { ...this.getFormDefaults(), ...optionValue };
    form.reset(
      {
        ...optionValueRawValue,
        id: { value: optionValueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OptionValueFormDefaults {
    return {
      id: null,
      settings: [],
    };
  }
}
