import { IOwner, NewOwner } from './owner.model';

export const sampleWithRequiredData: IOwner = {
  id: 8261,
  code: 'judgementally forenenst',
  firstName: 'Rachelle',
  lastName: 'Bauch',
};

export const sampleWithPartialData: IOwner = {
  id: 22041,
  code: 'firm waste notwithstanding',
  firstName: 'Moses',
  secondName: 'joyously yum unaccountably',
  lastName: 'Schuster',
  location: 'worth',
};

export const sampleWithFullData: IOwner = {
  id: 7659,
  code: 'hm deceivingly',
  firstName: 'Bernardo',
  secondName: 'enormously',
  lastName: 'Will',
  position: 'trusting delightful',
  location: 'squiggly instead hence',
};

export const sampleWithNewData: NewOwner = {
  code: 'pro lava nor',
  firstName: 'Noel',
  lastName: 'Heidenreich',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
