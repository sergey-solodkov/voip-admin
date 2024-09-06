import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISetting } from 'app/entities/setting/setting.model';
import { SettingService } from 'app/entities/setting/service/setting.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { OptionValueService } from '../service/option-value.service';
import { IOptionValue } from '../option-value.model';
import { OptionValueFormGroup, OptionValueFormService } from './option-value-form.service';

@Component({
  standalone: true,
  selector: 'jhi-option-value-update',
  templateUrl: './option-value-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OptionValueUpdateComponent implements OnInit {
  isSaving = false;
  optionValue: IOptionValue | null = null;

  settingsSharedCollection: ISetting[] = [];
  optionsSharedCollection: IOption[] = [];

  protected optionValueService = inject(OptionValueService);
  protected optionValueFormService = inject(OptionValueFormService);
  protected settingService = inject(SettingService);
  protected optionService = inject(OptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OptionValueFormGroup = this.optionValueFormService.createOptionValueFormGroup();

  compareSetting = (o1: ISetting | null, o2: ISetting | null): boolean => this.settingService.compareSetting(o1, o2);

  compareOption = (o1: IOption | null, o2: IOption | null): boolean => this.optionService.compareOption(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ optionValue }) => {
      this.optionValue = optionValue;
      if (optionValue) {
        this.updateForm(optionValue);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const optionValue = this.optionValueFormService.getOptionValue(this.editForm);
    if (optionValue.id !== null) {
      this.subscribeToSaveResponse(this.optionValueService.update(optionValue));
    } else {
      this.subscribeToSaveResponse(this.optionValueService.create(optionValue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOptionValue>>): void {
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

  protected updateForm(optionValue: IOptionValue): void {
    this.optionValue = optionValue;
    this.optionValueFormService.resetForm(this.editForm, optionValue);

    this.settingsSharedCollection = this.settingService.addSettingToCollectionIfMissing<ISetting>(
      this.settingsSharedCollection,
      ...(optionValue.settings ?? []),
    );
    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing<IOption>(
      this.optionsSharedCollection,
      optionValue.option,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.settingService
      .query()
      .pipe(map((res: HttpResponse<ISetting[]>) => res.body ?? []))
      .pipe(
        map((settings: ISetting[]) =>
          this.settingService.addSettingToCollectionIfMissing<ISetting>(settings, ...(this.optionValue?.settings ?? [])),
        ),
      )
      .subscribe((settings: ISetting[]) => (this.settingsSharedCollection = settings));

    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(map((options: IOption[]) => this.optionService.addOptionToCollectionIfMissing<IOption>(options, this.optionValue?.option)))
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));
  }
}
