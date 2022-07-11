import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmpresa, Empresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';
import { ICifrado } from 'app/entities/cifrado/cifrado.model';
import { CifradoService } from 'app/entities/cifrado/service/cifrado.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
/* eslint-disable no-console */
/* eslint-disable  */

@Component({
  selector: 'jhi-empresa-update',
  templateUrl: './empresa-update.component.html',
})
export class EmpresaUpdateComponent implements OnInit {
  isSaving = false;
  cifradosSharedCollection: ICifrado[] = [];
  verificaEmail: any;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    rif: [null, [Validators.required]],
    direccion: [null, [Validators.required]],
    telefono: [null, [Validators.required]],
    email: [null, [Validators.required, Validators.email]],
    cifrado: [],
  });

  constructor(
    protected empresaService: EmpresaService,
    protected cifradoService: CifradoService,
    protected activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empresa }) => {
      this.updateForm(empresa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empresa = this.createFromForm();
    if (empresa.id !== undefined) {
      this.subscribeToSaveResponse(this.empresaService.update(empresa));
    } else {
      this.subscribeToSaveResponse(this.empresaService.create(empresa));
    }

    this.empresaService.reporte(empresa.email, empresa.cifrado?.cifrado).subscribe((res: HttpResponse<String>) => {});
  }

  verificar(): void {
    this.verificaEmail = this.editForm.valid;
  }

  trackCifradoById(index: number, item: ICifrado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    // this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(empresa: IEmpresa): void {
    this.editForm.patchValue({
      id: empresa.id,
      nombre: empresa.nombre,
      rif: empresa.rif,
      direccion: empresa.direccion,
      telefono: empresa.telefono,
      email: empresa.email,
      cifrado: empresa.cifrado,
    });

    this.cifradosSharedCollection = this.cifradoService.addCifradoToCollectionIfMissing(this.cifradosSharedCollection, empresa.cifrado);
  }

  protected loadRelationshipsOptions(): void {
    this.cifradoService
      .query()
      .pipe(map((res: HttpResponse<ICifrado[]>) => res.body ?? []))
      .pipe(
        map((cifrados: ICifrado[]) => this.cifradoService.addCifradoToCollectionIfMissing(cifrados, this.editForm.get('cifrado')!.value))
      )
      .subscribe((cifrados: ICifrado[]) => (this.cifradosSharedCollection = cifrados));
  }

  protected createFromForm(): IEmpresa {
    return {
      ...new Empresa(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      rif: this.editForm.get(['rif'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      email: this.editForm.get(['email'])!.value,
      cifrado: this.editForm.get(['cifrado'])!.value,
    };
  }
}
