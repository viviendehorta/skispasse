import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SettingsComponent } from './settings/settings.component';
import { accountRoute } from './account.route';

@NgModule({
  imports: [RouterModule.forChild(accountRoute)],
  declarations: [SettingsComponent]
})
export class SkispasseAccountModule {}
