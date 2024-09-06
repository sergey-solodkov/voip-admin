import { IOptionValue, NewOptionValue } from './option-value.model';

export const sampleWithRequiredData: IOptionValue = {
  id: 31934,
};

export const sampleWithPartialData: IOptionValue = {
  id: 21906,
};

export const sampleWithFullData: IOptionValue = {
  id: 11529,
  value: 'zowie',
};

export const sampleWithNewData: NewOptionValue = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
