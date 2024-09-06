import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IOptionValue } from 'app/entities/option-value/option-value.model';
import { OptionValueService } from 'app/entities/option-value/service/option-value.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { SettingService } from '../service/setting.service';
import { ISetting } from '../setting.model';
import { SettingFormGroup, SettingFormService } from './setting-form.service';

@Component({
  standalone: true,
  selector: 'jhi-setting-update',
  templateUrl: './setting-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SettingUpdateComponent implements OnInit {
  isSaving = false;
  setting: ISetting | null = null;

  optionsSharedCollection: IOption[] = [];
  optionValuesSharedCollection: IOptionValue[] = [];
  devicesSharedCollection: IDevice[] = [];

  protected settingService = inject(SettingService);
  protected settingFormService = inject(SettingFormService);
  protected optionService = inject(OptionService);
  protected optionValueService = inject(OptionValueService);
  protected deviceService = inject(DeviceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SettingFormGroup = this.settingFormService.createSettingFormGroup();

  compareOption = (o1: IOption | null, o2: IOption | null): boolean => this.optionService.compareOption(o1, o2);

  compareOptionValue = (o1: IOptionValue | null, o2: IOptionValue | null): boolean => this.optionValueService.compareOptionValue(o1, o2);

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setting }) => {
      this.setting = setting;
      if (setting) {
        this.updateForm(setting);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const setting = this.settingFormService.getSetting(this.editForm);
    if (setting.id !== null) {
      this.subscribeToSaveResponse(this.settingService.update(setting));
    } else {
      this.subscribeToSaveResponse(this.settingService.create(setting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISetting>>): void {
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

  protected updateForm(setting: ISetting): void {
    this.setting = setting;
    this.settingFormService.resetForm(this.editForm, setting);

    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing<IOption>(this.optionsSharedCollection, setting.option);
    this.optionValuesSharedCollection = this.optionValueService.addOptionValueToCollectionIfMissing<IOptionValue>(
      this.optionValuesSharedCollection,
      ...(setting.selectedValues ?? []),
    );
    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(this.devicesSharedCollection, setting.device);
  }

  protected loadRelationshipsOptions(): void {
    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(map((options: IOption[]) => this.optionService.addOptionToCollectionIfMissing<IOption>(options, this.setting?.option)))
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));

    this.optionValueService
      .query()
      .pipe(map((res: HttpResponse<IOptionValue[]>) => res.body ?? []))
      .pipe(
        map((optionValues: IOptionValue[]) =>
          this.optionValueService.addOptionValueToCollectionIfMissing<IOptionValue>(optionValues, ...(this.setting?.selectedValues ?? [])),
        ),
      )
      .subscribe((optionValues: IOptionValue[]) => (this.optionValuesSharedCollection = optionValues));

    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.setting?.device)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
