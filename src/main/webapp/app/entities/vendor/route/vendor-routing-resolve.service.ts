import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVendor } from '../vendor.model';
import { VendorService } from '../service/vendor.service';

const vendorResolve = (route: ActivatedRouteSnapshot): Observable<null | IVendor> => {
  const id = route.params.id;
  if (id) {
    return inject(VendorService)
      .find(id)
      .pipe(
        mergeMap((vendor: HttpResponse<IVendor>) => {
          if (vendor.body) {
            return of(vendor.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vendorResolve;
