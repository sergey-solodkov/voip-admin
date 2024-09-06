import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOptionValue, NewOptionValue } from '../option-value.model';

export type PartialUpdateOptionValue = Partial<IOptionValue> & Pick<IOptionValue, 'id'>;

export type EntityResponseType = HttpResponse<IOptionValue>;
export type EntityArrayResponseType = HttpResponse<IOptionValue[]>;

@Injectable({ providedIn: 'root' })
export class OptionValueService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/option-values');

  create(optionValue: NewOptionValue): Observable<EntityResponseType> {
    return this.http.post<IOptionValue>(this.resourceUrl, optionValue, { observe: 'response' });
  }

  update(optionValue: IOptionValue): Observable<EntityResponseType> {
    return this.http.put<IOptionValue>(`${this.resourceUrl}/${this.getOptionValueIdentifier(optionValue)}`, optionValue, {
      observe: 'response',
    });
  }

  partialUpdate(optionValue: PartialUpdateOptionValue): Observable<EntityResponseType> {
    return this.http.patch<IOptionValue>(`${this.resourceUrl}/${this.getOptionValueIdentifier(optionValue)}`, optionValue, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOptionValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOptionValue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOptionValueIdentifier(optionValue: Pick<IOptionValue, 'id'>): number {
    return optionValue.id;
  }

  compareOptionValue(o1: Pick<IOptionValue, 'id'> | null, o2: Pick<IOptionValue, 'id'> | null): boolean {
    return o1 && o2 ? this.getOptionValueIdentifier(o1) === this.getOptionValueIdentifier(o2) : o1 === o2;
  }

  addOptionValueToCollectionIfMissing<Type extends Pick<IOptionValue, 'id'>>(
    optionValueCollection: Type[],
    ...optionValuesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const optionValues: Type[] = optionValuesToCheck.filter(isPresent);
    if (optionValues.length > 0) {
      const optionValueCollectionIdentifiers = optionValueCollection.map(optionValueItem => this.getOptionValueIdentifier(optionValueItem));
      const optionValuesToAdd = optionValues.filter(optionValueItem => {
        const optionValueIdentifier = this.getOptionValueIdentifier(optionValueItem);
        if (optionValueCollectionIdentifiers.includes(optionValueIdentifier)) {
          return false;
        }
        optionValueCollectionIdentifiers.push(optionValueIdentifier);
        return true;
      });
      return [...optionValuesToAdd, ...optionValueCollection];
    }
    return optionValueCollection;
  }
}
