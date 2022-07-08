import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICifrado, getCifradoIdentifier } from '../cifrado.model';

export type EntityResponseType = HttpResponse<ICifrado>;
export type EntityArrayResponseType = HttpResponse<ICifrado[]>;

@Injectable({ providedIn: 'root' })
export class CifradoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cifrados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cifrado: ICifrado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cifrado);
    return this.http
      .post<ICifrado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cifrado: ICifrado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cifrado);
    return this.http
      .put<ICifrado>(`${this.resourceUrl}/${getCifradoIdentifier(cifrado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cifrado: ICifrado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cifrado);
    return this.http
      .patch<ICifrado>(`${this.resourceUrl}/${getCifradoIdentifier(cifrado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICifrado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICifrado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCifradoToCollectionIfMissing(cifradoCollection: ICifrado[], ...cifradosToCheck: (ICifrado | null | undefined)[]): ICifrado[] {
    const cifrados: ICifrado[] = cifradosToCheck.filter(isPresent);
    if (cifrados.length > 0) {
      const cifradoCollectionIdentifiers = cifradoCollection.map(cifradoItem => getCifradoIdentifier(cifradoItem)!);
      const cifradosToAdd = cifrados.filter(cifradoItem => {
        const cifradoIdentifier = getCifradoIdentifier(cifradoItem);
        if (cifradoIdentifier == null || cifradoCollectionIdentifiers.includes(cifradoIdentifier)) {
          return false;
        }
        cifradoCollectionIdentifiers.push(cifradoIdentifier);
        return true;
      });
      return [...cifradosToAdd, ...cifradoCollection];
    }
    return cifradoCollection;
  }

  protected convertDateFromClient(cifrado: ICifrado): ICifrado {
    return Object.assign({}, cifrado, {
      fechaVencimiento: cifrado.fechaVencimiento?.isValid() ? cifrado.fechaVencimiento.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaVencimiento = res.body.fechaVencimiento ? dayjs(res.body.fechaVencimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cifrado: ICifrado) => {
        cifrado.fechaVencimiento = cifrado.fechaVencimiento ? dayjs(cifrado.fechaVencimiento) : undefined;
      });
    }
    return res;
  }
}
