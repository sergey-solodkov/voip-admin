import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVoipAccount } from '../voip-account.model';
import { VoipAccountService } from '../service/voip-account.service';

const voipAccountResolve = (route: ActivatedRouteSnapshot): Observable<null | IVoipAccount> => {
  const id = route.params.id;
  if (id) {
    return inject(VoipAccountService)
      .find(id)
      .pipe(
        mergeMap((voipAccount: HttpResponse<IVoipAccount>) => {
          if (voipAccount.body) {
            return of(voipAccount.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default voipAccountResolve;
