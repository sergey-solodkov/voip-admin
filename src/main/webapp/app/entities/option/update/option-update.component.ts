import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { DeviceModelService } from 'app/entities/device-model/service/device-model.service';
import { OptionValueType } from 'app/entities/enumerations/option-value-type.model';
import { OptionService } from '../service/option.service';
import { IOption } from '../option.model';
import { OptionFormGroup, OptionFormService } from './option-form.service';

@Component({
  standalone: true,
  selector: 'jhi-option-update',
  templateUrl: './option-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OptionUpdateComponent implements OnInit {
  isSaving = false;
  option: IOption | null = null;
  optionValueTypeValues = Object.keys(OptionValueType);

  vendorsSharedCollection: IVendor[] = [];
  deviceModelsSharedCollection: IDeviceModel[] = [];

  protected optionService = inject(OptionService);
  protected optionFormService = inject(OptionFormService);
  protected vendorService = inject(VendorService);
  protected deviceModelService = inject(DeviceModelService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OptionFormGroup = this.optionFormService.createOptionFormGroup();

  compareVendor = (o1: IVendor | null, o2: IVendor | null): boolean => this.vendorService.compareVendor(o1, o2);

  compareDeviceModel = (o1: IDeviceModel | null, o2: IDeviceModel | null): boolean => this.deviceModelService.compareDeviceModel(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ option }) => {
      this.option = option;
      if (option) {
        this.updateForm(option);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const option = this.optionFormService.getOption(this.editForm);
    if (option.id !== null) {
      this.subscribeToSaveResponse(this.optionService.update(option));
    } else {
      this.subscribeToSaveResponse(this.optionService.create(option));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOption>>): void {
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

  protected updateForm(option: IOption): void {
    this.option = option;
    this.optionFormService.resetForm(this.editForm, option);

    this.vendorsSharedCollection = this.vendorService.addVendorToCollectionIfMissing<IVendor>(
      this.vendorsSharedCollection,
      ...(option.vendors ?? []),
    );
    this.deviceModelsSharedCollection = this.deviceModelService.addDeviceModelToCollectionIfMissing<IDeviceModel>(
      this.deviceModelsSharedCollection,
      ...(option.models ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vendorService
      .query()
      .pipe(map((res: HttpResponse<IVendor[]>) => res.body ?? []))
      .pipe(
        map((vendors: IVendor[]) => this.vendorService.addVendorToCollectionIfMissing<IVendor>(vendors, ...(this.option?.vendors ?? []))),
      )
      .subscribe((vendors: IVendor[]) => (this.vendorsSharedCollection = vendors));

    this.deviceModelService
      .query()
      .pipe(map((res: HttpResponse<IDeviceModel[]>) => res.body ?? []))
      .pipe(
        map((deviceModels: IDeviceModel[]) =>
          this.deviceModelService.addDeviceModelToCollectionIfMissing<IDeviceModel>(deviceModels, ...(this.option?.models ?? [])),
        ),
      )
      .subscribe((deviceModels: IDeviceModel[]) => (this.deviceModelsSharedCollection = deviceModels));
  }
}
