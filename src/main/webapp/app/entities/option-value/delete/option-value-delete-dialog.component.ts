import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOptionValue } from '../option-value.model';
import { OptionValueService } from '../service/option-value.service';

@Component({
  standalone: true,
  templateUrl: './option-value-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OptionValueDeleteDialogComponent {
  optionValue?: IOptionValue;

  protected optionValueService = inject(OptionValueService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.optionValueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
