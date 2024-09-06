import { IOption, NewOption } from './option.model';

export const sampleWithRequiredData: IOption = {
  id: 21226,
};

export const sampleWithPartialData: IOption = {
  id: 2655,
  valueType: 'TEXT',
};

export const sampleWithFullData: IOption = {
  id: 29624,
  code: 'standardization',
  descr: 'and zowie',
  valueType: 'TEXT',
  multiple: false,
};

export const sampleWithNewData: NewOption = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
