import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeService } from '../service/other-device-type.service';
import { OtherDeviceTypeFormGroup, OtherDeviceTypeFormService } from './other-device-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-other-device-type-update',
  templateUrl: './other-device-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OtherDeviceTypeUpdateComponent implements OnInit {
  isSaving = false;
  otherDeviceType: IOtherDeviceType | null = null;

  protected otherDeviceTypeService = inject(OtherDeviceTypeService);
  protected otherDeviceTypeFormService = inject(OtherDeviceTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OtherDeviceTypeFormGroup = this.otherDeviceTypeFormService.createOtherDeviceTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ otherDeviceType }) => {
      this.otherDeviceType = otherDeviceType;
      if (otherDeviceType) {
        this.updateForm(otherDeviceType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const otherDeviceType = this.otherDeviceTypeFormService.getOtherDeviceType(this.editForm);
    if (otherDeviceType.id !== null) {
      this.subscribeToSaveResponse(this.otherDeviceTypeService.update(otherDeviceType));
    } else {
      this.subscribeToSaveResponse(this.otherDeviceTypeService.create(otherDeviceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOtherDeviceType>>): void {
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

  protected updateForm(otherDeviceType: IOtherDeviceType): void {
    this.otherDeviceType = otherDeviceType;
    this.otherDeviceTypeFormService.resetForm(this.editForm, otherDeviceType);
  }
}
