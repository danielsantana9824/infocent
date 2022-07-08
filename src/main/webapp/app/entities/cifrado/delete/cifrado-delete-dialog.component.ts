import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICifrado } from '../cifrado.model';
import { CifradoService } from '../service/cifrado.service';

@Component({
  templateUrl: './cifrado-delete-dialog.component.html',
})
export class CifradoDeleteDialogComponent {
  cifrado?: ICifrado;

  constructor(protected cifradoService: CifradoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cifradoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
