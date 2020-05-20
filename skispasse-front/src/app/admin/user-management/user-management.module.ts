import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UserManagementComponent } from './user-management.component';
import { UserManagementDetailComponent } from './user-management-detail.component';
import { UserManagementUpdateComponent } from './user-management-update.component';
import { UserManagementDeleteDialogComponent } from './user-management-delete-dialog.component';
import { userManagementRoutes } from './user-management.routes';
import {SkispasseSharedModule} from '../../shared/shared.module';

@NgModule({
  imports: [SkispasseSharedModule, RouterModule.forChild(userManagementRoutes)],
  declarations: [
    UserManagementComponent,
    UserManagementDetailComponent,
    UserManagementUpdateComponent,
    UserManagementDeleteDialogComponent
  ],
  entryComponents: [UserManagementDeleteDialogComponent]
})
export class UserManagementModule {}
