import { IDeviceModel, NewDeviceModel } from './device-model.model';

export const sampleWithRequiredData: IDeviceModel = {
  id: 17594,
  name: 'wasabi naturally',
  configurable: true,
};

export const sampleWithPartialData: IDeviceModel = {
  id: 2172,
  name: 'inside knead',
  configurable: true,
  configTemplatePath: 'upwardly lest doubtfully',
  deviceType: 'IPPHONE',
};

export const sampleWithFullData: IDeviceModel = {
  id: 11155,
  name: 'duh desert',
  configurable: false,
  linesAmount: 23021,
  configTemplatePath: 'who to',
  firmwareFilePath: 'quickly atelier telecast',
  deviceType: 'IPGATEWAY',
};

export const sampleWithNewData: NewDeviceModel = {
  name: 'questioningly weekly',
  configurable: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
