import { NgModule } from '@angular/core';
import { SkispasseSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { SkisAlertComponent } from './alert/alert.component';
import { SkisAlertErrorComponent } from './alert/alert-error.component';

@NgModule({
  imports: [SkispasseSharedLibsModule],
  declarations: [FindLanguageFromKeyPipe, SkisAlertComponent, SkisAlertErrorComponent],
  exports: [SkispasseSharedLibsModule, FindLanguageFromKeyPipe, SkisAlertComponent, SkisAlertErrorComponent]
})
export class SkispasseSharedModule {}
