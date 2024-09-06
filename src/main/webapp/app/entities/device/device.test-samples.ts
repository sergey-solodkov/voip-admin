import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 10006,
  mac: 'precious oh indeed',
};

export const sampleWithPartialData: IDevice = {
  id: 3850,
  mac: 'frenetically',
  inventoryId: 'behind less manservant',
  hostname: 'thicken hog',
  webAccessLogin: 'journey why bag',
  subnetMask: 'skipper analgesia',
  defaultGw: 'although ack casino',
  dns1: 'quickly',
  dns2: 'blah gladly unless',
  provisioningMode: 'FTP',
  configPath: 'pay formal',
  notes: 'including peaceful',
};

export const sampleWithFullData: IDevice = {
  id: 20992,
  mac: 'hence a queasily',
  inventoryId: 'reminisce long-term upbeat',
  location: 'tunnel remaster wherever',
  hostname: 'editor',
  webAccessLogin: 'hence amongst revolving',
  webAccessPasswordHash: 'grown ick meanwhile',
  dhcpEnabled: true,
  ipAddress: 'quarrelsomely',
  subnetMask: 'capital',
  defaultGw: 'across supposing kiwi',
  dns1: 'without',
  dns2: 'oof duh prejudge',
  provisioningMode: 'HTTP',
  provisioningUrl: 'demand or',
  ntp: 'pace',
  configPath: 'apud till playfully',
  notes: 'obtain',
};

export const sampleWithNewData: NewDevice = {
  mac: 'internal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
