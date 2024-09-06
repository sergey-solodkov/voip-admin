import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'voipadminApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'device',
    data: { pageTitle: 'voipadminApp.device.home.title' },
    loadChildren: () => import('./device/device.routes'),
  },
  {
    path: 'device-model',
    data: { pageTitle: 'voipadminApp.deviceModel.home.title' },
    loadChildren: () => import('./device-model/device-model.routes'),
  },
  {
    path: 'other-device-type',
    data: { pageTitle: 'voipadminApp.otherDeviceType.home.title' },
    loadChildren: () => import('./other-device-type/other-device-type.routes'),
  },
  {
    path: 'owner',
    data: { pageTitle: 'voipadminApp.owner.home.title' },
    loadChildren: () => import('./owner/owner.routes'),
  },
  {
    path: 'department',
    data: { pageTitle: 'voipadminApp.department.home.title' },
    loadChildren: () => import('./department/department.routes'),
  },
  {
    path: 'voip-account',
    data: { pageTitle: 'voipadminApp.voipAccount.home.title' },
    loadChildren: () => import('./voip-account/voip-account.routes'),
  },
  {
    path: 'setting',
    data: { pageTitle: 'voipadminApp.setting.home.title' },
    loadChildren: () => import('./setting/setting.routes'),
  },
  {
    path: 'option',
    data: { pageTitle: 'voipadminApp.option.home.title' },
    loadChildren: () => import('./option/option.routes'),
  },
  {
    path: 'option-value',
    data: { pageTitle: 'voipadminApp.optionValue.home.title' },
    loadChildren: () => import('./option-value/option-value.routes'),
  },
  {
    path: 'vendor',
    data: { pageTitle: 'voipadminApp.vendor.home.title' },
    loadChildren: () => import('./vendor/vendor.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
