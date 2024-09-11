import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { IOwner } from 'app/entities/owner/owner.model';
import { ProvisioningMode } from 'app/entities/enumerations/provisioning-mode.model';
import { IVoipAccount } from '../voip-account/voip-account.model';

export interface IDevice {
  id: number;
  mac?: string | null;
  inventoryId?: string | null;
  location?: string | null;
  hostname?: string | null;
  webAccessLogin?: string | null;
  webAccessPasswordHash?: string | null;
  dhcpEnabled?: boolean | null;
  ipAddress?: string | null;
  subnetMask?: string | null;
  defaultGw?: string | null;
  dns1?: string | null;
  dns2?: string | null;
  provisioningMode?: keyof typeof ProvisioningMode | null;
  provisioningUrl?: string | null;
  ntp?: string | null;
  configPath?: string | null;
  notes?: string | null;
  model?: Pick<IDeviceModel, 'id' | 'name'> | null;
  owner?: Pick<IOwner, 'id' | 'lastName'> | null;
  parent?: Pick<IDevice, 'id'> | null;
  voipAccounts?: Pick<IVoipAccount, 'id' | 'username'>[] | null;
}

export type NewDevice = Omit<IDevice, 'id'> & { id: null };
