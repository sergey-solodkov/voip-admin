import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDeviceModel } from '../device-model.model';

@Component({
  standalone: true,
  selector: 'jhi-device-model-detail',
  templateUrl: './device-model-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DeviceModelDetailComponent {
  deviceModel = input<IDeviceModel | null>(null);

  previousState(): void {
    window.history.back();
  }
}
