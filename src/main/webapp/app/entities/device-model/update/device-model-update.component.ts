import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOtherDeviceType } from 'app/entities/other-device-type/other-device-type.model';
import { OtherDeviceTypeService } from 'app/entities/other-device-type/service/other-device-type.service';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { DeviceType } from 'app/entities/enumerations/device-type.model';
import { DeviceModelService } from '../service/device-model.service';
import { IDeviceModel } from '../device-model.model';
import { DeviceModelFormGroup, DeviceModelFormService } from './device-model-form.service';

@Component({
  standalone: true,
  selector: 'jhi-device-model-update',
  templateUrl: './device-model-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeviceModelUpdateComponent implements OnInit {
  isSaving = false;
  deviceModel: IDeviceModel | null = null;
  deviceTypeValues = Object.keys(DeviceType);

  otherDeviceTypesSharedCollection: IOtherDeviceType[] = [];
  vendorsSharedCollection: IVendor[] = [];
  optionsSharedCollection: IOption[] = [];

  protected deviceModelService = inject(DeviceModelService);
  protected deviceModelFormService = inject(DeviceModelFormService);
  protected otherDeviceTypeService = inject(OtherDeviceTypeService);
  protected vendorService = inject(VendorService);
  protected optionService = inject(OptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeviceModelFormGroup = this.deviceModelFormService.createDeviceModelFormGroup();

  compareOtherDeviceType = (o1: IOtherDeviceType | null, o2: IOtherDeviceType | null): boolean =>
    this.otherDeviceTypeService.compareOtherDeviceType(o1, o2);

  compareVendor = (o1: IVendor | null, o2: IVendor | null): boolean => this.vendorService.compareVendor(o1, o2);

  compareOption = (o1: IOption | null, o2: IOption | null): boolean => this.optionService.compareOption(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceModel }) => {
      this.deviceModel = deviceModel;
      if (deviceModel) {
        this.updateForm(deviceModel);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceModel = this.deviceModelFormService.getDeviceModel(this.editForm);
    if (deviceModel.id !== null) {
      this.subscribeToSaveResponse(this.deviceModelService.update(deviceModel));
    } else {
      this.subscribeToSaveResponse(this.deviceModelService.create(deviceModel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceModel>>): void {
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

  protected updateForm(deviceModel: IDeviceModel): void {
    this.deviceModel = deviceModel;
    this.deviceModelFormService.resetForm(this.editForm, deviceModel);

    this.otherDeviceTypesSharedCollection = this.otherDeviceTypeService.addOtherDeviceTypeToCollectionIfMissing<IOtherDeviceType>(
      this.otherDeviceTypesSharedCollection,
      deviceModel.otherDeviceType,
    );
    this.vendorsSharedCollection = this.vendorService.addVendorToCollectionIfMissing<IVendor>(
      this.vendorsSharedCollection,
      deviceModel.vendor,
    );
    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing<IOption>(
      this.optionsSharedCollection,
      ...(deviceModel.options ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.otherDeviceTypeService
      .query()
      .pipe(map((res: HttpResponse<IOtherDeviceType[]>) => res.body ?? []))
      .pipe(
        map((otherDeviceTypes: IOtherDeviceType[]) =>
          this.otherDeviceTypeService.addOtherDeviceTypeToCollectionIfMissing<IOtherDeviceType>(
            otherDeviceTypes,
            this.deviceModel?.otherDeviceType,
          ),
        ),
      )
      .subscribe((otherDeviceTypes: IOtherDeviceType[]) => (this.otherDeviceTypesSharedCollection = otherDeviceTypes));

    this.vendorService
      .query()
      .pipe(map((res: HttpResponse<IVendor[]>) => res.body ?? []))
      .pipe(map((vendors: IVendor[]) => this.vendorService.addVendorToCollectionIfMissing<IVendor>(vendors, this.deviceModel?.vendor)))
      .subscribe((vendors: IVendor[]) => (this.vendorsSharedCollection = vendors));

    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(
        map((options: IOption[]) =>
          this.optionService.addOptionToCollectionIfMissing<IOption>(options, ...(this.deviceModel?.options ?? [])),
        ),
      )
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));
  }
}
