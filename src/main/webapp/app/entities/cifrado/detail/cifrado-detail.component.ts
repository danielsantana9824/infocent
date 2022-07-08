import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICifrado } from '../cifrado.model';

@Component({
  selector: 'jhi-cifrado-detail',
  templateUrl: './cifrado-detail.component.html',
})
export class CifradoDetailComponent implements OnInit {
  cifrado: ICifrado | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cifrado }) => {
      this.cifrado = cifrado;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
