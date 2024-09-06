import { IDepartment } from 'app/entities/department/department.model';

export interface IOwner {
  id: number;
  code?: string | null;
  firstName?: string | null;
  secondName?: string | null;
  lastName?: string | null;
  position?: string | null;
  location?: string | null;
  department?: Pick<IDepartment, 'id' | 'name'> | null;
}

export type NewOwner = Omit<IOwner, 'id'> & { id: null };
