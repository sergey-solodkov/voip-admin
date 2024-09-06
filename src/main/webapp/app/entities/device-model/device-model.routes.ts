import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DeviceModelResolve from './route/device-model-routing-resolve.service';

const deviceModelRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/device-model.component').then(m => m.DeviceModelComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/device-model-detail.component').then(m => m.DeviceModelDetailComponent),
    resolve: {
      deviceModel: DeviceModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/device-model-update.component').then(m => m.DeviceModelUpdateComponent),
    resolve: {
      deviceModel: DeviceModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/device-model-update.component').then(m => m.DeviceModelUpdateComponent),
    resolve: {
      deviceModel: DeviceModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default deviceModelRoute;
