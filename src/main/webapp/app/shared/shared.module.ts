import { NgModule } from '@angular/core';
import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { SkisAlertComponent } from './alert/alert.component';
import { SkisAlertErrorComponent } from './alert/alert-error.component';

@NgModule({
  imports: [SharedLibsModule],
  declarations: [FindLanguageFromKeyPipe, SkisAlertComponent, SkisAlertErrorComponent],
  exports: [SharedLibsModule, FindLanguageFromKeyPipe, SkisAlertComponent, SkisAlertErrorComponent]
})
export class SkispasseSharedModule {}
