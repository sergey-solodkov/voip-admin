import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SettingResolve from './route/setting-routing-resolve.service';

const settingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/setting.component').then(m => m.SettingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/setting-detail.component').then(m => m.SettingDetailComponent),
    resolve: {
      setting: SettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/setting-update.component').then(m => m.SettingUpdateComponent),
    resolve: {
      setting: SettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/setting-update.component').then(m => m.SettingUpdateComponent),
    resolve: {
      setting: SettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default settingRoute;
