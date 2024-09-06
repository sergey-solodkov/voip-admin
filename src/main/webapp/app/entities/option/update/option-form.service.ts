import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOption, NewOption } from '../option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOption for edit and NewOptionFormGroupInput for create.
 */
type OptionFormGroupInput = IOption | PartialWithRequiredKeyOf<NewOption>;

type OptionFormDefaults = Pick<NewOption, 'id' | 'multiple' | 'vendors' | 'models'>;

type OptionFormGroupContent = {
  id: FormControl<IOption['id'] | NewOption['id']>;
  code: FormControl<IOption['code']>;
  descr: FormControl<IOption['descr']>;
  valueType: FormControl<IOption['valueType']>;
  multiple: FormControl<IOption['multiple']>;
  vendors: FormControl<IOption['vendors']>;
  models: FormControl<IOption['models']>;
};

export type OptionFormGroup = FormGroup<OptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OptionFormService {
  createOptionFormGroup(option: OptionFormGroupInput = { id: null }): OptionFormGroup {
    const optionRawValue = {
      ...this.getFormDefaults(),
      ...option,
    };
    return new FormGroup<OptionFormGroupContent>({
      id: new FormControl(
        { value: optionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(optionRawValue.code),
      descr: new FormControl(optionRawValue.descr),
      valueType: new FormControl(optionRawValue.valueType),
      multiple: new FormControl(optionRawValue.multiple),
      vendors: new FormControl(optionRawValue.vendors ?? []),
      models: new FormControl(optionRawValue.models ?? []),
    });
  }

  getOption(form: OptionFormGroup): IOption | NewOption {
    return form.getRawValue() as IOption | NewOption;
  }

  resetForm(form: OptionFormGroup, option: OptionFormGroupInput): void {
    const optionRawValue = { ...this.getFormDefaults(), ...option };
    form.reset(
      {
        ...optionRawValue,
        id: { value: optionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OptionFormDefaults {
    return {
      id: null,
      multiple: false,
      vendors: [],
      models: [],
    };
  }
}
