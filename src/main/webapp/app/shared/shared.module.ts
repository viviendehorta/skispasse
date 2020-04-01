import { NgModule } from '@angular/core';
import { SkispasseSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { SkisAlertComponent } from './alert/alert.component';
import { SkisAlertErrorComponent } from './alert/alert-error.component';
import { SkisLoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';

@NgModule({
  imports: [SkispasseSharedLibsModule],
  declarations: [FindLanguageFromKeyPipe, SkisAlertComponent, SkisAlertErrorComponent, SkisLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [SkisLoginModalComponent],
  exports: [
    SkispasseSharedLibsModule,
    FindLanguageFromKeyPipe,
    SkisAlertComponent,
    SkisAlertErrorComponent,
    SkisLoginModalComponent,
    HasAnyAuthorityDirective
  ]
})
export class SkispasseSharedModule {}
