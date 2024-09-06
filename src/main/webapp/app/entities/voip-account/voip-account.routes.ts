import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VoipAccountResolve from './route/voip-account-routing-resolve.service';

const voipAccountRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/voip-account.component').then(m => m.VoipAccountComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/voip-account-detail.component').then(m => m.VoipAccountDetailComponent),
    resolve: {
      voipAccount: VoipAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/voip-account-update.component').then(m => m.VoipAccountUpdateComponent),
    resolve: {
      voipAccount: VoipAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/voip-account-update.component').then(m => m.VoipAccountUpdateComponent),
    resolve: {
      voipAccount: VoipAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default voipAccountRoute;
