import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOtherDeviceType, NewOtherDeviceType } from '../other-device-type.model';

export type PartialUpdateOtherDeviceType = Partial<IOtherDeviceType> & Pick<IOtherDeviceType, 'id'>;

export type EntityResponseType = HttpResponse<IOtherDeviceType>;
export type EntityArrayResponseType = HttpResponse<IOtherDeviceType[]>;

@Injectable({ providedIn: 'root' })
export class OtherDeviceTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/other-device-types');

  create(otherDeviceType: NewOtherDeviceType): Observable<EntityResponseType> {
    return this.http.post<IOtherDeviceType>(this.resourceUrl, otherDeviceType, { observe: 'response' });
  }

  update(otherDeviceType: IOtherDeviceType): Observable<EntityResponseType> {
    return this.http.put<IOtherDeviceType>(`${this.resourceUrl}/${this.getOtherDeviceTypeIdentifier(otherDeviceType)}`, otherDeviceType, {
      observe: 'response',
    });
  }

  partialUpdate(otherDeviceType: PartialUpdateOtherDeviceType): Observable<EntityResponseType> {
    return this.http.patch<IOtherDeviceType>(`${this.resourceUrl}/${this.getOtherDeviceTypeIdentifier(otherDeviceType)}`, otherDeviceType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOtherDeviceType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOtherDeviceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOtherDeviceTypeIdentifier(otherDeviceType: Pick<IOtherDeviceType, 'id'>): number {
    return otherDeviceType.id;
  }

  compareOtherDeviceType(o1: Pick<IOtherDeviceType, 'id'> | null, o2: Pick<IOtherDeviceType, 'id'> | null): boolean {
    return o1 && o2 ? this.getOtherDeviceTypeIdentifier(o1) === this.getOtherDeviceTypeIdentifier(o2) : o1 === o2;
  }

  addOtherDeviceTypeToCollectionIfMissing<Type extends Pick<IOtherDeviceType, 'id'>>(
    otherDeviceTypeCollection: Type[],
    ...otherDeviceTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const otherDeviceTypes: Type[] = otherDeviceTypesToCheck.filter(isPresent);
    if (otherDeviceTypes.length > 0) {
      const otherDeviceTypeCollectionIdentifiers = otherDeviceTypeCollection.map(otherDeviceTypeItem =>
        this.getOtherDeviceTypeIdentifier(otherDeviceTypeItem),
      );
      const otherDeviceTypesToAdd = otherDeviceTypes.filter(otherDeviceTypeItem => {
        const otherDeviceTypeIdentifier = this.getOtherDeviceTypeIdentifier(otherDeviceTypeItem);
        if (otherDeviceTypeCollectionIdentifiers.includes(otherDeviceTypeIdentifier)) {
          return false;
        }
        otherDeviceTypeCollectionIdentifiers.push(otherDeviceTypeIdentifier);
        return true;
      });
      return [...otherDeviceTypesToAdd, ...otherDeviceTypeCollection];
    }
    return otherDeviceTypeCollection;
  }
}
