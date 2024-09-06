import { IVoipAccount, NewVoipAccount } from './voip-account.model';

export const sampleWithRequiredData: IVoipAccount = {
  id: 14131,
};

export const sampleWithPartialData: IVoipAccount = {
  id: 17881,
  manual: false,
  username: 'cat until',
  sipPort: 'near loose',
  lineEnable: false,
};

export const sampleWithFullData: IVoipAccount = {
  id: 11379,
  manual: true,
  username: 'phew what faithfully',
  passwordHash: 'grab after famously',
  sipServer: 'hunt including gosh',
  sipPort: 'sore',
  lineEnable: false,
  lineNumber: 30751,
};

export const sampleWithNewData: NewVoipAccount = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
