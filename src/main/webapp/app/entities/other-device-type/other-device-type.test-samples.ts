import { IOtherDeviceType, NewOtherDeviceType } from './other-device-type.model';

export const sampleWithRequiredData: IOtherDeviceType = {
  id: 6158,
};

export const sampleWithPartialData: IOtherDeviceType = {
  id: 27967,
};

export const sampleWithFullData: IOtherDeviceType = {
  id: 4926,
  name: 'utterly biodegradable firm',
  description: 'unsteady hm',
};

export const sampleWithNewData: NewOtherDeviceType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
