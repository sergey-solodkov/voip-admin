import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 21806,
  name: 'developing',
};

export const sampleWithPartialData: IDepartment = {
  id: 21134,
  name: 'untried mid soupy',
};

export const sampleWithFullData: IDepartment = {
  id: 6109,
  name: 'after maroon',
};

export const sampleWithNewData: NewDepartment = {
  name: 'toward oh pacify',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
