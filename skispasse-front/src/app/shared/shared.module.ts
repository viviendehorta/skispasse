import {NgModule} from '@angular/core';
import {SharedLibsModule} from './shared-libs.module';
import {FindLanguageFromKeyPipe} from './language/find-language-from-key.pipe';
import {AlertComponent} from './alert/alert.component';
import {AlertErrorComponent} from './alert/alert-error.component';
import {SortDirective} from './sort/sort.directive';
import {SortByDirective} from './sort/sort-by.directive';
import {BooleanFieldComponent} from './form/boolean-field/boolean-field.component';
import {VideoPlayerComponent} from "./video/video-player.component";
import {PageHeaderComponent} from "./layout/page-header/page-header.component";
import {RouterModule} from "@angular/router";

@NgModule({
    declarations: [
        AlertComponent,
        AlertErrorComponent,
        BooleanFieldComponent,
        FindLanguageFromKeyPipe,
        PageHeaderComponent,
        SortDirective,
        SortByDirective,
        VideoPlayerComponent
    ],
    imports: [RouterModule, SharedLibsModule],
    exports: [
        AlertComponent,
        AlertErrorComponent,
        BooleanFieldComponent,
        FindLanguageFromKeyPipe,
        PageHeaderComponent,
        SharedLibsModule,
        SortDirective,
        SortByDirective,
        VideoPlayerComponent
    ]
})
export class SkispasseSharedModule {
}
