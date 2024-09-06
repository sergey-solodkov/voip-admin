import { IOption } from 'app/entities/option/option.model';

export interface IVendor {
  id: number;
  name?: string | null;
  options?: Pick<IOption, 'id' | 'code'>[] | null;
}

export type NewVendor = Omit<IVendor, 'id'> & { id: null };
