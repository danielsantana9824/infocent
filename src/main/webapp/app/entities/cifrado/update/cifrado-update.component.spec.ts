import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CifradoService } from '../service/cifrado.service';
import { ICifrado, Cifrado } from '../cifrado.model';

import { CifradoUpdateComponent } from './cifrado-update.component';

describe('Cifrado Management Update Component', () => {
  let comp: CifradoUpdateComponent;
  let fixture: ComponentFixture<CifradoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cifradoService: CifradoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CifradoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CifradoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CifradoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cifradoService = TestBed.inject(CifradoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cifrado: ICifrado = { id: 456 };

      activatedRoute.data = of({ cifrado });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cifrado));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cifrado>>();
      const cifrado = { id: 123 };
      jest.spyOn(cifradoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cifrado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cifrado }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cifradoService.update).toHaveBeenCalledWith(cifrado);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cifrado>>();
      const cifrado = new Cifrado();
      jest.spyOn(cifradoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cifrado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cifrado }));
      saveSubject.complete();

      // THEN
      expect(cifradoService.create).toHaveBeenCalledWith(cifrado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cifrado>>();
      const cifrado = { id: 123 };
      jest.spyOn(cifradoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cifrado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cifradoService.update).toHaveBeenCalledWith(cifrado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
