import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVendor } from '../vendor.model';
import { VendorService } from '../service/vendor.service';

@Component({
  standalone: true,
  templateUrl: './vendor-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VendorDeleteDialogComponent {
  vendor?: IVendor;

  protected vendorService = inject(VendorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vendorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
