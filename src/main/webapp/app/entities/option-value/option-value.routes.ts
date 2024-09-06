import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OptionValueResolve from './route/option-value-routing-resolve.service';

const optionValueRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/option-value.component').then(m => m.OptionValueComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/option-value-detail.component').then(m => m.OptionValueDetailComponent),
    resolve: {
      optionValue: OptionValueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/option-value-update.component').then(m => m.OptionValueUpdateComponent),
    resolve: {
      optionValue: OptionValueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/option-value-update.component').then(m => m.OptionValueUpdateComponent),
    resolve: {
      optionValue: OptionValueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default optionValueRoute;
