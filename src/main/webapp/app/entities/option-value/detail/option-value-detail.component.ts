import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOptionValue } from '../option-value.model';

@Component({
  standalone: true,
  selector: 'jhi-option-value-detail',
  templateUrl: './option-value-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OptionValueDetailComponent {
  optionValue = input<IOptionValue | null>(null);

  previousState(): void {
    window.history.back();
  }
}
