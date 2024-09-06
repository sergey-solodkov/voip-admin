export interface IOtherDeviceType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewOtherDeviceType = Omit<IOtherDeviceType, 'id'> & { id: null };
