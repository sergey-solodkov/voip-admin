import { ISetting, NewSetting } from './setting.model';

export const sampleWithRequiredData: ISetting = {
  id: 22659,
};

export const sampleWithPartialData: ISetting = {
  id: 6785,
  textValue: 'reprocessing experiment',
};

export const sampleWithFullData: ISetting = {
  id: 17119,
  textValue: 'under oh hence',
};

export const sampleWithNewData: NewSetting = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
