import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 22232,
  login: 'zM@7JVgU0\\#hY\\xT305',
};

export const sampleWithPartialData: IUser = {
  id: 20887,
  login: 'Nc.NE',
};

export const sampleWithFullData: IUser = {
  id: 23356,
  login: '2',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
