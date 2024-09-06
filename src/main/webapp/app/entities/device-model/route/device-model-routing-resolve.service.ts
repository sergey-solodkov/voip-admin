import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeviceModel } from '../device-model.model';
import { DeviceModelService } from '../service/device-model.service';

const deviceModelResolve = (route: ActivatedRouteSnapshot): Observable<null | IDeviceModel> => {
  const id = route.params.id;
  if (id) {
    return inject(DeviceModelService)
      .find(id)
      .pipe(
        mergeMap((deviceModel: HttpResponse<IDeviceModel>) => {
          if (deviceModel.body) {
            return of(deviceModel.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default deviceModelResolve;
