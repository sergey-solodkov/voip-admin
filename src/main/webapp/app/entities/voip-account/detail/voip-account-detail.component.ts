import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVoipAccount } from '../voip-account.model';

@Component({
  standalone: true,
  selector: 'jhi-voip-account-detail',
  templateUrl: './voip-account-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VoipAccountDetailComponent {
  voipAccount = input<IVoipAccount | null>(null);

  previousState(): void {
    window.history.back();
  }
}
