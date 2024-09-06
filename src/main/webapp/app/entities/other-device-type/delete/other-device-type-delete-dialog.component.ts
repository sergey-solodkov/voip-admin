import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeService } from '../service/other-device-type.service';

@Component({
  standalone: true,
  templateUrl: './other-device-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OtherDeviceTypeDeleteDialogComponent {
  otherDeviceType?: IOtherDeviceType;

  protected otherDeviceTypeService = inject(OtherDeviceTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.otherDeviceTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
