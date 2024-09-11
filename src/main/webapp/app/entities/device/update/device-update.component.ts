import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormArray, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { DeviceModelService } from 'app/entities/device-model/service/device-model.service';
import { IOwner } from 'app/entities/owner/owner.model';
import { OwnerService } from 'app/entities/owner/service/owner.service';
import { ProvisioningMode } from 'app/entities/enumerations/provisioning-mode.model';
import { DeviceService } from '../service/device.service';
import { IDevice } from '../device.model';
import { DeviceFormGroup, DeviceFormService } from './device-form.service';
import { IVoipAccount } from 'app/entities/voip-account/voip-account.model';
import { VoipAccountFormService } from 'app/entities/voip-account/update/voip-account-form.service';
import { DeviceModelChangeDialogComponent } from './device-model-change-dialog/device-model-change-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: true,
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;
  device: IDevice | null = null;
  provisioningModeValues = Object.keys(ProvisioningMode);
  oldModelValue: Pick<IDeviceModel, 'id' | 'name'> | null | undefined;

  devicesSharedCollection: IDevice[] = [];
  deviceModelsSharedCollection: IDeviceModel[] = [];
  ownersSharedCollection: IOwner[] = [];

  protected deviceService = inject(DeviceService);
  protected deviceFormService = inject(DeviceFormService);
  protected deviceModelService = inject(DeviceModelService);
  protected ownerService = inject(OwnerService);
  protected activatedRoute = inject(ActivatedRoute);
  protected voipAccountFormService = inject(VoipAccountFormService);
  protected modalService = inject(NgbModal);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeviceFormGroup = this.deviceFormService.createDeviceFormGroup();

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  compareDeviceModel = (o1: IDeviceModel | null, o2: IDeviceModel | null): boolean => this.deviceModelService.compareDeviceModel(o1, o2);

  compareOwner = (o1: IOwner | null, o2: IOwner | null): boolean => this.ownerService.compareOwner(o1, o2);

  get voipAccounts(): FormArray {
    return this.editForm.get('voipAccounts') as FormArray;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.device = device;
      if (device) {
        this.updateForm(device);
        this.oldModelValue = device.model;
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const device = this.deviceFormService.getDevice(this.editForm);
    if (device.id !== null) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  onDeviceModelChange(): void {
    if (this.oldModelValue) {
      const modalRef = this.modalService.open(DeviceModelChangeDialogComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.oldValue = this.oldModelValue;
      modalRef.componentInstance.newValue = this.editForm.get('model')?.value;
      modalRef.closed.subscribe(result => {
        this.editForm.patchValue({
          model: result,
        });
        if (result !== this.oldModelValue) {
          this.voipAccounts.clear();
          this.initVoipAccounts();
        }
        this.oldModelValue = result;
      });
    } else {
      this.oldModelValue = this.editForm.get('model')?.value;
      this.initVoipAccounts();
    }
  }

  initVoipAccounts(voipAccounts?: IVoipAccount[]): void {
    if (!this.editForm.get('model')?.value) {
      this.voipAccounts.clear();
      return;
    }

    const deviceModel = this.deviceModelsSharedCollection.find(model => model.id === this.editForm.get('model')?.value?.id);
    if (deviceModel?.configurable && deviceModel.linesAmount! > 0) {
      if (voipAccounts && voipAccounts.length > 0) {
        voipAccounts
          .sort((account1, account2) => account1.lineNumber! - account2.lineNumber!)
          .forEach(account => this.addVoipAccount(account));
      } else {
        for (let i = 0; i < deviceModel.linesAmount!; i++) {
          this.addVoipAccount();
        }
      }
    }
  }

  addVoipAccount(voipAccount?: IVoipAccount): void {
    this.voipAccounts.push(this.voipAccountFormService.createVoipAccountFormGroup(voipAccount));
  }

  resetVoipAccount(index: number): void {
    this.voipAccounts.get(index.toString())?.reset();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
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

  protected updateForm(device: IDevice): void {
    this.device = device;
    this.deviceFormService.resetForm(this.editForm, device);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(this.devicesSharedCollection, device.parent);
    this.deviceModelsSharedCollection = this.deviceModelService.addDeviceModelToCollectionIfMissing<IDeviceModel>(
      this.deviceModelsSharedCollection,
      device.model,
    );
    this.ownersSharedCollection = this.ownerService.addOwnerToCollectionIfMissing<IOwner>(this.ownersSharedCollection, device.owner);
    if (device.model) {
      this.initVoipAccounts(device.voipAccounts!);
    }
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.device?.parent)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));

    this.deviceModelService
      .query()
      .pipe(map((res: HttpResponse<IDeviceModel[]>) => res.body ?? []))
      .pipe(
        map((deviceModels: IDeviceModel[]) =>
          this.deviceModelService.addDeviceModelToCollectionIfMissing<IDeviceModel>(deviceModels, this.device?.model),
        ),
      )
      .subscribe((deviceModels: IDeviceModel[]) => (this.deviceModelsSharedCollection = deviceModels));

    this.ownerService
      .query()
      .pipe(map((res: HttpResponse<IOwner[]>) => res.body ?? []))
      .pipe(map((owners: IOwner[]) => this.ownerService.addOwnerToCollectionIfMissing<IOwner>(owners, this.device?.owner)))
      .subscribe((owners: IOwner[]) => (this.ownersSharedCollection = owners));
  }
}
