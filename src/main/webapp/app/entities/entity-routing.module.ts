import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cifrado',
        data: { pageTitle: 'Cifrados' },
        loadChildren: () => import('./cifrado/cifrado.module').then(m => m.CifradoModule),
      },
      {
        path: 'empresa',
        data: { pageTitle: 'Empresas' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
