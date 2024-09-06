import { IDevice } from 'app/entities/device/device.model';

export interface IVoipAccount {
  id: number;
  manual?: boolean | null;
  username?: string | null;
  passwordHash?: string | null;
  sipServer?: string | null;
  sipPort?: string | null;
  lineEnable?: boolean | null;
  lineNumber?: number | null;
  device?: Pick<IDevice, 'id'> | null;
}

export type NewVoipAccount = Omit<IVoipAccount, 'id'> & { id: null };
