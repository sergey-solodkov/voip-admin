import { IVendor, NewVendor } from './vendor.model';

export const sampleWithRequiredData: IVendor = {
  id: 29712,
};

export const sampleWithPartialData: IVendor = {
  id: 28886,
};

export const sampleWithFullData: IVendor = {
  id: 3204,
  name: 'now and',
};

export const sampleWithNewData: NewVendor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
