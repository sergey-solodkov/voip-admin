import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOwner } from '../owner.model';

@Component({
  standalone: true,
  selector: 'jhi-owner-detail',
  templateUrl: './owner-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OwnerDetailComponent {
  owner = input<IOwner | null>(null);

  previousState(): void {
    window.history.back();
  }
}
