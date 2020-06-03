import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {VgCoreModule} from "videogular2/compiled/src/core/core";
import {VgControlsModule} from "videogular2/compiled/src/controls/controls";
import {VgBufferingModule} from "videogular2/compiled/src/buffering/buffering";
import {VgOverlayPlayModule} from "videogular2/compiled/src/overlay-play/overlay-play";

@NgModule({
    exports: [
        FormsModule,
        CommonModule,
        NgbModule,
        FontAwesomeModule,
        ReactiveFormsModule,
        VgCoreModule,
        VgControlsModule,
        VgOverlayPlayModule,
        VgBufferingModule
    ]
})
export class SharedLibsModule {
}
