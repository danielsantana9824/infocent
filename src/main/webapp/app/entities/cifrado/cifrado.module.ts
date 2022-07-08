import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CifradoComponent } from './list/cifrado.component';
import { CifradoDetailComponent } from './detail/cifrado-detail.component';
import { CifradoUpdateComponent } from './update/cifrado-update.component';
import { CifradoDeleteDialogComponent } from './delete/cifrado-delete-dialog.component';
import { CifradoRoutingModule } from './route/cifrado-routing.module';

@NgModule({
  imports: [SharedModule, CifradoRoutingModule],
  declarations: [CifradoComponent, CifradoDetailComponent, CifradoUpdateComponent, CifradoDeleteDialogComponent],
  entryComponents: [CifradoDeleteDialogComponent],
})
export class CifradoModule {}
