import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOptionValue } from '../option-value.model';
import { OptionValueService } from '../service/option-value.service';

const optionValueResolve = (route: ActivatedRouteSnapshot): Observable<null | IOptionValue> => {
  const id = route.params.id;
  if (id) {
    return inject(OptionValueService)
      .find(id)
      .pipe(
        mergeMap((optionValue: HttpResponse<IOptionValue>) => {
          if (optionValue.body) {
            return of(optionValue.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default optionValueResolve;
