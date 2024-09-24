import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeviceModel, NewDeviceModel } from '../device-model.model';

export type PartialUpdateDeviceModel = Partial<IDeviceModel> & Pick<IDeviceModel, 'id'>;

export type EntityResponseType = HttpResponse<IDeviceModel>;
export type EntityArrayResponseType = HttpResponse<IDeviceModel[]>;

@Injectable({ providedIn: 'root' })
export class DeviceModelService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/device-models');

  create(deviceModel: NewDeviceModel): Observable<EntityResponseType> {
    return this.http.post<IDeviceModel>(this.resourceUrl, deviceModel, { observe: 'response' });
  }

  update(deviceModel: IDeviceModel): Observable<EntityResponseType> {
    return this.http.put<IDeviceModel>(`${this.resourceUrl}/${this.getDeviceModelIdentifier(deviceModel)}`, deviceModel, {
      observe: 'response',
    });
  }

  partialUpdate(deviceModel: PartialUpdateDeviceModel): Observable<EntityResponseType> {
    return this.http.patch<IDeviceModel>(`${this.resourceUrl}/${this.getDeviceModelIdentifier(deviceModel)}`, deviceModel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeviceModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConfigTemplate(id: number): Observable<HttpResponse<Blob>> {
    return this.http.get(`${this.resourceUrl}/${id}/config-template`, { observe: 'response', responseType: 'blob' });
  }

  getFirmwareFile(id: number): Observable<HttpResponse<Blob>> {
    return this.http.get(`${this.resourceUrl}/${id}/firmware-file`, { observe: 'response', responseType: 'blob' });
  }

  getDeviceModelIdentifier(deviceModel: Pick<IDeviceModel, 'id'>): number {
    return deviceModel.id;
  }

  compareDeviceModel(o1: Pick<IDeviceModel, 'id'> | null, o2: Pick<IDeviceModel, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeviceModelIdentifier(o1) === this.getDeviceModelIdentifier(o2) : o1 === o2;
  }

  addDeviceModelToCollectionIfMissing<Type extends Pick<IDeviceModel, 'id'>>(
    deviceModelCollection: Type[],
    ...deviceModelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deviceModels: Type[] = deviceModelsToCheck.filter(isPresent);
    if (deviceModels.length > 0) {
      const deviceModelCollectionIdentifiers = deviceModelCollection.map(deviceModelItem => this.getDeviceModelIdentifier(deviceModelItem));
      const deviceModelsToAdd = deviceModels.filter(deviceModelItem => {
        const deviceModelIdentifier = this.getDeviceModelIdentifier(deviceModelItem);
        if (deviceModelCollectionIdentifiers.includes(deviceModelIdentifier)) {
          return false;
        }
        deviceModelCollectionIdentifiers.push(deviceModelIdentifier);
        return true;
      });
      return [...deviceModelsToAdd, ...deviceModelCollection];
    }
    return deviceModelCollection;
  }
}
