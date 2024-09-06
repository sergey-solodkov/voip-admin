import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDeviceModel } from '../device-model.model';
import { DeviceModelService } from '../service/device-model.service';

@Component({
  standalone: true,
  templateUrl: './device-model-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DeviceModelDeleteDialogComponent {
  deviceModel?: IDeviceModel;

  protected deviceModelService = inject(DeviceModelService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceModelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
