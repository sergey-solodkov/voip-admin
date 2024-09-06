import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OtherDeviceTypeResolve from './route/other-device-type-routing-resolve.service';

const otherDeviceTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/other-device-type.component').then(m => m.OtherDeviceTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/other-device-type-detail.component').then(m => m.OtherDeviceTypeDetailComponent),
    resolve: {
      otherDeviceType: OtherDeviceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/other-device-type-update.component').then(m => m.OtherDeviceTypeUpdateComponent),
    resolve: {
      otherDeviceType: OtherDeviceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/other-device-type-update.component').then(m => m.OtherDeviceTypeUpdateComponent),
    resolve: {
      otherDeviceType: OtherDeviceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default otherDeviceTypeRoute;
