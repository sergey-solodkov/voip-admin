import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '4c4e56d0-6fa8-4a13-a4e9-98de0d373633',
};

export const sampleWithPartialData: IAuthority = {
  name: '0e6edccd-1ff7-4a18-baa1-3b5fb2e5e4fd',
};

export const sampleWithFullData: IAuthority = {
  name: '42cb5cde-2beb-4def-af3d-51a161f1e215',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
