import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVoipAccount } from '../voip-account.model';
import { VoipAccountService } from '../service/voip-account.service';

@Component({
  standalone: true,
  templateUrl: './voip-account-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VoipAccountDeleteDialogComponent {
  voipAccount?: IVoipAccount;

  protected voipAccountService = inject(VoipAccountService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.voipAccountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
