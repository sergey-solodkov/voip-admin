import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IVendor } from 'app/entities/vendor/vendor.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  templateUrl: './vendor-change-dialog.component.html',
  imports: [SharedModule],
})
export class VendorChangeDialogComponent {
  oldValue?: IVendor;
  newValue?: IVendor;

  private activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.close(this.oldValue);
  }

  confirmVendorChange(): void {
    this.activeModal.close(this.newValue);
  }
}
