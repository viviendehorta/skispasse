import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SettingsComponent } from './settings.component';
import { settingsRoute } from './settings.route';
import { SkispasseSharedModule } from 'app/shared/shared.module';
import { PasswordStrengthBarComponent } from 'app/user/settings/password-strength-bar.component';

@NgModule({
  imports: [SkispasseSharedModule, RouterModule.forChild(settingsRoute)],
  declarations: [PasswordStrengthBarComponent, SettingsComponent]
})
export class SettingsModule {}
