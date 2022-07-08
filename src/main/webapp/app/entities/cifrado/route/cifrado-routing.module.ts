import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CifradoComponent } from '../list/cifrado.component';
import { CifradoDetailComponent } from '../detail/cifrado-detail.component';
import { CifradoUpdateComponent } from '../update/cifrado-update.component';
import { CifradoRoutingResolveService } from './cifrado-routing-resolve.service';

const cifradoRoute: Routes = [
  {
    path: '',
    component: CifradoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CifradoDetailComponent,
    resolve: {
      cifrado: CifradoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CifradoUpdateComponent,
    resolve: {
      cifrado: CifradoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CifradoUpdateComponent,
    resolve: {
      cifrado: CifradoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cifradoRoute)],
  exports: [RouterModule],
})
export class CifradoRoutingModule {}
