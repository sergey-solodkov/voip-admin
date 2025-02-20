import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOtherDeviceType } from '../other-device-type.model';

@Component({
  standalone: true,
  selector: 'jhi-other-device-type-detail',
  templateUrl: './other-device-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OtherDeviceTypeDetailComponent {
  otherDeviceType = input<IOtherDeviceType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
