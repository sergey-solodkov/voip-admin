import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVoipAccount, NewVoipAccount } from '../voip-account.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVoipAccount for edit and NewVoipAccountFormGroupInput for create.
 */
type VoipAccountFormGroupInput = IVoipAccount | PartialWithRequiredKeyOf<NewVoipAccount>;

type VoipAccountFormDefaults = Pick<NewVoipAccount, 'id' | 'manual' | 'lineEnable'>;

type VoipAccountFormGroupContent = {
  id: FormControl<IVoipAccount['id'] | NewVoipAccount['id']>;
  manual: FormControl<IVoipAccount['manual']>;
  username: FormControl<IVoipAccount['username']>;
  passwordHash: FormControl<IVoipAccount['passwordHash']>;
  sipServer: FormControl<IVoipAccount['sipServer']>;
  sipPort: FormControl<IVoipAccount['sipPort']>;
  lineEnable: FormControl<IVoipAccount['lineEnable']>;
  lineNumber: FormControl<IVoipAccount['lineNumber']>;
  device: FormControl<IVoipAccount['device']>;
};

export type VoipAccountFormGroup = FormGroup<VoipAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VoipAccountFormService {
  createVoipAccountFormGroup(voipAccount: VoipAccountFormGroupInput = { id: null }): VoipAccountFormGroup {
    const voipAccountRawValue = {
      ...this.getFormDefaults(),
      ...voipAccount,
    };
    return new FormGroup<VoipAccountFormGroupContent>({
      id: new FormControl(
        { value: voipAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      manual: new FormControl(voipAccountRawValue.manual),
      username: new FormControl(voipAccountRawValue.username),
      passwordHash: new FormControl(voipAccountRawValue.passwordHash),
      sipServer: new FormControl(voipAccountRawValue.sipServer),
      sipPort: new FormControl(voipAccountRawValue.sipPort),
      lineEnable: new FormControl(voipAccountRawValue.lineEnable),
      lineNumber: new FormControl(voipAccountRawValue.lineNumber),
      device: new FormControl(voipAccountRawValue.device),
    });
  }

  getVoipAccount(form: VoipAccountFormGroup): IVoipAccount | NewVoipAccount {
    return form.getRawValue() as IVoipAccount | NewVoipAccount;
  }

  resetForm(form: VoipAccountFormGroup, voipAccount: VoipAccountFormGroupInput): void {
    const voipAccountRawValue = { ...this.getFormDefaults(), ...voipAccount };
    form.reset(
      {
        ...voipAccountRawValue,
        id: { value: voipAccountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VoipAccountFormDefaults {
    return {
      id: null,
      manual: false,
      lineEnable: false,
    };
  }
}
