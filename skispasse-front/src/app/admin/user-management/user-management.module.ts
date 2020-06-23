import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {UserManagementComponent} from './user-management.component';
import {UserManagementDetailComponent} from './user-management-detail.component';
import {UserManagementDeleteDialogComponent} from './user-management-delete-dialog.component';
import {userManagementRoutes} from './user-management.routes';
import {SkispasseSharedModule} from '../../shared/shared.module';
import {UserFormComponent} from "./user-form/user-form.component";
import {UserCreationComponent} from "./create-user/user-creation.component";
import {UserEditionComponent} from "./update-user/user-edition.component";

@NgModule({
    imports: [SkispasseSharedModule, RouterModule.forChild(userManagementRoutes)],
    declarations: [
        UserCreationComponent,
        UserEditionComponent,
        UserFormComponent,
        UserManagementComponent,
        UserManagementDeleteDialogComponent,
        UserManagementDetailComponent
    ],
    entryComponents: [UserManagementDeleteDialogComponent]
})
export class UserManagementModule {
}
