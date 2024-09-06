import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IVendor } from '../vendor.model';
import { VendorService } from '../service/vendor.service';
import { VendorFormGroup, VendorFormService } from './vendor-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vendor-update',
  templateUrl: './vendor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VendorUpdateComponent implements OnInit {
  isSaving = false;
  vendor: IVendor | null = null;

  optionsSharedCollection: IOption[] = [];

  protected vendorService = inject(VendorService);
  protected vendorFormService = inject(VendorFormService);
  protected optionService = inject(OptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VendorFormGroup = this.vendorFormService.createVendorFormGroup();

  compareOption = (o1: IOption | null, o2: IOption | null): boolean => this.optionService.compareOption(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendor }) => {
      this.vendor = vendor;
      if (vendor) {
        this.updateForm(vendor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendor = this.vendorFormService.getVendor(this.editForm);
    if (vendor.id !== null) {
      this.subscribeToSaveResponse(this.vendorService.update(vendor));
    } else {
      this.subscribeToSaveResponse(this.vendorService.create(vendor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendor>>): void {
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

  protected updateForm(vendor: IVendor): void {
    this.vendor = vendor;
    this.vendorFormService.resetForm(this.editForm, vendor);

    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing<IOption>(
      this.optionsSharedCollection,
      ...(vendor.options ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(
        map((options: IOption[]) => this.optionService.addOptionToCollectionIfMissing<IOption>(options, ...(this.vendor?.options ?? []))),
      )
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));
  }
}
