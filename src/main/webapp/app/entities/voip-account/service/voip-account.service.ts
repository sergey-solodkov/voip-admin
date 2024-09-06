import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVoipAccount, NewVoipAccount } from '../voip-account.model';

export type PartialUpdateVoipAccount = Partial<IVoipAccount> & Pick<IVoipAccount, 'id'>;

export type EntityResponseType = HttpResponse<IVoipAccount>;
export type EntityArrayResponseType = HttpResponse<IVoipAccount[]>;

@Injectable({ providedIn: 'root' })
export class VoipAccountService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/voip-accounts');

  create(voipAccount: NewVoipAccount): Observable<EntityResponseType> {
    return this.http.post<IVoipAccount>(this.resourceUrl, voipAccount, { observe: 'response' });
  }

  update(voipAccount: IVoipAccount): Observable<EntityResponseType> {
    return this.http.put<IVoipAccount>(`${this.resourceUrl}/${this.getVoipAccountIdentifier(voipAccount)}`, voipAccount, {
      observe: 'response',
    });
  }

  partialUpdate(voipAccount: PartialUpdateVoipAccount): Observable<EntityResponseType> {
    return this.http.patch<IVoipAccount>(`${this.resourceUrl}/${this.getVoipAccountIdentifier(voipAccount)}`, voipAccount, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVoipAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVoipAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVoipAccountIdentifier(voipAccount: Pick<IVoipAccount, 'id'>): number {
    return voipAccount.id;
  }

  compareVoipAccount(o1: Pick<IVoipAccount, 'id'> | null, o2: Pick<IVoipAccount, 'id'> | null): boolean {
    return o1 && o2 ? this.getVoipAccountIdentifier(o1) === this.getVoipAccountIdentifier(o2) : o1 === o2;
  }

  addVoipAccountToCollectionIfMissing<Type extends Pick<IVoipAccount, 'id'>>(
    voipAccountCollection: Type[],
    ...voipAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const voipAccounts: Type[] = voipAccountsToCheck.filter(isPresent);
    if (voipAccounts.length > 0) {
      const voipAccountCollectionIdentifiers = voipAccountCollection.map(voipAccountItem => this.getVoipAccountIdentifier(voipAccountItem));
      const voipAccountsToAdd = voipAccounts.filter(voipAccountItem => {
        const voipAccountIdentifier = this.getVoipAccountIdentifier(voipAccountItem);
        if (voipAccountCollectionIdentifiers.includes(voipAccountIdentifier)) {
          return false;
        }
        voipAccountCollectionIdentifiers.push(voipAccountIdentifier);
        return true;
      });
      return [...voipAccountsToAdd, ...voipAccountCollection];
    }
    return voipAccountCollection;
  }
}
