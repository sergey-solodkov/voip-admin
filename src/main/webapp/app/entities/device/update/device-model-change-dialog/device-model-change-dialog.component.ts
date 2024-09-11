import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDeviceModel } from '../../../device-model/device-model.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  templateUrl: './device-model-change-dialog.component.html',
  imports: [SharedModule],
})
export class DeviceModelChangeDialogComponent {
  oldValue?: IDeviceModel;
  newValue?: IDeviceModel;

  private activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.close(this.oldValue);
  }

  confirmModelChange(): void {
    this.activeModal.close(this.newValue);
  }
}
