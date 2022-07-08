import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICifrado, Cifrado } from '../cifrado.model';
import { CifradoService } from '../service/cifrado.service';

@Injectable({ providedIn: 'root' })
export class CifradoRoutingResolveService implements Resolve<ICifrado> {
  constructor(protected service: CifradoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICifrado> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cifrado: HttpResponse<Cifrado>) => {
          if (cifrado.body) {
            return of(cifrado.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cifrado());
  }
}
