import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeService } from '../service/other-device-type.service';

const otherDeviceTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IOtherDeviceType> => {
  const id = route.params.id;
  if (id) {
    return inject(OtherDeviceTypeService)
      .find(id)
      .pipe(
        mergeMap((otherDeviceType: HttpResponse<IOtherDeviceType>) => {
          if (otherDeviceType.body) {
            return of(otherDeviceType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default otherDeviceTypeResolve;
