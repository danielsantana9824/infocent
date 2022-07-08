import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CifradoDetailComponent } from './cifrado-detail.component';

describe('Cifrado Management Detail Component', () => {
  let comp: CifradoDetailComponent;
  let fixture: ComponentFixture<CifradoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CifradoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cifrado: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CifradoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CifradoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cifrado on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cifrado).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
