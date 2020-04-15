import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SettingsComponent } from './settings/settings.component';
import { accountRoute } from './account.route';
import { SkispasseSharedModule } from 'app/shared/shared.module';
import { PasswordStrengthBarComponent } from 'app/account/settings/password-strength-bar.component';

@NgModule({
  imports: [SkispasseSharedModule, RouterModule.forChild(accountRoute)],
  declarations: [PasswordStrengthBarComponent, SettingsComponent]
})
export class SkispasseAccountModule {}
