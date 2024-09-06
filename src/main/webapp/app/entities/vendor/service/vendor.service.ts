import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVendor, NewVendor } from '../vendor.model';

export type PartialUpdateVendor = Partial<IVendor> & Pick<IVendor, 'id'>;

export type EntityResponseType = HttpResponse<IVendor>;
export type EntityArrayResponseType = HttpResponse<IVendor[]>;

@Injectable({ providedIn: 'root' })
export class VendorService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vendors');

  create(vendor: NewVendor): Observable<EntityResponseType> {
    return this.http.post<IVendor>(this.resourceUrl, vendor, { observe: 'response' });
  }

  update(vendor: IVendor): Observable<EntityResponseType> {
    return this.http.put<IVendor>(`${this.resourceUrl}/${this.getVendorIdentifier(vendor)}`, vendor, { observe: 'response' });
  }

  partialUpdate(vendor: PartialUpdateVendor): Observable<EntityResponseType> {
    return this.http.patch<IVendor>(`${this.resourceUrl}/${this.getVendorIdentifier(vendor)}`, vendor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVendor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVendor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVendorIdentifier(vendor: Pick<IVendor, 'id'>): number {
    return vendor.id;
  }

  compareVendor(o1: Pick<IVendor, 'id'> | null, o2: Pick<IVendor, 'id'> | null): boolean {
    return o1 && o2 ? this.getVendorIdentifier(o1) === this.getVendorIdentifier(o2) : o1 === o2;
  }

  addVendorToCollectionIfMissing<Type extends Pick<IVendor, 'id'>>(
    vendorCollection: Type[],
    ...vendorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vendors: Type[] = vendorsToCheck.filter(isPresent);
    if (vendors.length > 0) {
      const vendorCollectionIdentifiers = vendorCollection.map(vendorItem => this.getVendorIdentifier(vendorItem));
      const vendorsToAdd = vendors.filter(vendorItem => {
        const vendorIdentifier = this.getVendorIdentifier(vendorItem);
        if (vendorCollectionIdentifiers.includes(vendorIdentifier)) {
          return false;
        }
        vendorCollectionIdentifiers.push(vendorIdentifier);
        return true;
      });
      return [...vendorsToAdd, ...vendorCollection];
    }
    return vendorCollection;
  }
}
