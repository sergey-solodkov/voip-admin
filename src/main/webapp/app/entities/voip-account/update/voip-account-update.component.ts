import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IVoipAccount } from '../voip-account.model';
import { VoipAccountService } from '../service/voip-account.service';
import { VoipAccountFormGroup, VoipAccountFormService } from './voip-account-form.service';

@Component({
  standalone: true,
  selector: 'jhi-voip-account-update',
  templateUrl: './voip-account-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VoipAccountUpdateComponent implements OnInit {
  isSaving = false;
  voipAccount: IVoipAccount | null = null;

  devicesSharedCollection: IDevice[] = [];

  protected voipAccountService = inject(VoipAccountService);
  protected voipAccountFormService = inject(VoipAccountFormService);
  protected deviceService = inject(DeviceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VoipAccountFormGroup = this.voipAccountFormService.createVoipAccountFormGroup();

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voipAccount }) => {
      this.voipAccount = voipAccount;
      if (voipAccount) {
        this.updateForm(voipAccount);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const voipAccount = this.voipAccountFormService.getVoipAccount(this.editForm);
    if (voipAccount.id !== null) {
      this.subscribeToSaveResponse(this.voipAccountService.update(voipAccount));
    } else {
      this.subscribeToSaveResponse(this.voipAccountService.create(voipAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVoipAccount>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(voipAccount: IVoipAccount): void {
    this.voipAccount = voipAccount;
    this.voipAccountFormService.resetForm(this.editForm, voipAccount);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
      this.devicesSharedCollection,
      voipAccount.device,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.voipAccount?.device)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
