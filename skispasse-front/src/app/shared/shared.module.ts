import {NgModule} from '@angular/core';
import {SharedLibsModule} from './shared-libs.module';
import {FindLanguageFromKeyPipe} from './language/find-language-from-key.pipe';
import {AlertComponent} from './alert/alert.component';
import {AlertErrorComponent} from './alert/alert-error.component';
import {SortDirective} from './sort/sort.directive';
import {SortByDirective} from './sort/sort-by.directive';
import {ItemCountComponent} from './pagination/item-count/item-count.component';
import {BooleanFieldComponent} from './form/boolean-field/boolean-field.component';

@NgModule({
  declarations: [
    AlertComponent,
    BooleanFieldComponent,
    FindLanguageFromKeyPipe,
    AlertErrorComponent,
    ItemCountComponent,
    SortDirective,
    SortByDirective
  ],
  imports: [SharedLibsModule],
  exports: [
    AlertComponent,
    AlertErrorComponent,
    BooleanFieldComponent,
    FindLanguageFromKeyPipe,
    ItemCountComponent,
    SharedLibsModule,
    SortDirective,
    SortByDirective
  ]
})
export class SkispasseSharedModule {
}
