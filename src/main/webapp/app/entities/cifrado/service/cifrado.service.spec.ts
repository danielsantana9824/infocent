import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICifrado, Cifrado } from '../cifrado.model';

import { CifradoService } from './cifrado.service';

describe('Cifrado Service', () => {
  let service: CifradoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICifrado;
  let expectedResult: ICifrado | ICifrado[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CifradoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      cifrado: 'AAAAAAA',
      fechaVencimiento: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaVencimiento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Cifrado', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaVencimiento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.create(new Cifrado()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cifrado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          cifrado: 'BBBBBB',
          fechaVencimiento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cifrado', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        new Cifrado()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cifrado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          cifrado: 'BBBBBB',
          fechaVencimiento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Cifrado', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCifradoToCollectionIfMissing', () => {
      it('should add a Cifrado to an empty array', () => {
        const cifrado: ICifrado = { id: 123 };
        expectedResult = service.addCifradoToCollectionIfMissing([], cifrado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cifrado);
      });

      it('should not add a Cifrado to an array that contains it', () => {
        const cifrado: ICifrado = { id: 123 };
        const cifradoCollection: ICifrado[] = [
          {
            ...cifrado,
          },
          { id: 456 },
        ];
        expectedResult = service.addCifradoToCollectionIfMissing(cifradoCollection, cifrado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cifrado to an array that doesn't contain it", () => {
        const cifrado: ICifrado = { id: 123 };
        const cifradoCollection: ICifrado[] = [{ id: 456 }];
        expectedResult = service.addCifradoToCollectionIfMissing(cifradoCollection, cifrado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cifrado);
      });

      it('should add only unique Cifrado to an array', () => {
        const cifradoArray: ICifrado[] = [{ id: 123 }, { id: 456 }, { id: 71431 }];
        const cifradoCollection: ICifrado[] = [{ id: 123 }];
        expectedResult = service.addCifradoToCollectionIfMissing(cifradoCollection, ...cifradoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cifrado: ICifrado = { id: 123 };
        const cifrado2: ICifrado = { id: 456 };
        expectedResult = service.addCifradoToCollectionIfMissing([], cifrado, cifrado2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cifrado);
        expect(expectedResult).toContain(cifrado2);
      });

      it('should accept null and undefined values', () => {
        const cifrado: ICifrado = { id: 123 };
        expectedResult = service.addCifradoToCollectionIfMissing([], null, cifrado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cifrado);
      });

      it('should return initial array if no Cifrado is added', () => {
        const cifradoCollection: ICifrado[] = [{ id: 123 }];
        expectedResult = service.addCifradoToCollectionIfMissing(cifradoCollection, undefined, null);
        expect(expectedResult).toEqual(cifradoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
