import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VendorResolve from './route/vendor-routing-resolve.service';

const vendorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vendor.component').then(m => m.VendorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vendor-detail.component').then(m => m.VendorDetailComponent),
    resolve: {
      vendor: VendorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vendor-update.component').then(m => m.VendorUpdateComponent),
    resolve: {
      vendor: VendorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vendor-update.component').then(m => m.VendorUpdateComponent),
    resolve: {
      vendor: VendorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vendorRoute;
