import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICifrado, Cifrado } from '../cifrado.model';
import { CifradoService } from '../service/cifrado.service';
import moment from 'moment';
/* eslint-disable no-console */

@Component({
  selector: 'jhi-cifrado-update',
  templateUrl: './cifrado-update.component.html',
})
export class CifradoUpdateComponent implements OnInit {
  isSaving = false;
  fecha : any;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    descripcion: [null, [Validators.required]],
    cifrado: [null, [Validators.required]],
    fechaVencimiento: [null, [Validators.required]],
  });

  constructor(protected cifradoService: CifradoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.fecha = {};
    this.fecha = moment().add(90, 'd');

    
    this.activatedRoute.data.subscribe(({ cifrado }) => {
      this.updateForm(cifrado);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cifrado = this.createFromForm();

    if (cifrado.id !== undefined) {
      this.subscribeToSaveResponse(this.cifradoService.update(cifrado));
    } else {
      this.subscribeToSaveResponse(this.cifradoService.create(cifrado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICifrado>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cifrado: ICifrado): void {

    this.editForm.patchValue({
      id: cifrado.id,
      nombre: cifrado.nombre,
      descripcion: cifrado.descripcion,
      cifrado: cifrado.cifrado,
      fechaVencimiento: this.fecha
    });
  }

  protected createFromForm(): ICifrado {
    return {
      ...new Cifrado(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      cifrado: this.editForm.get(['cifrado'])!.value,
      fechaVencimiento: this.fecha
    };
  }
}
