import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {SkispasseSharedModule} from './shared/shared.module';
import {SkispasseCoreModule} from './core/core.module';
import {SkispasseAppRoutingModule} from './app-routing.module';
import {WorldmapModule} from './public/worldmap/worldmap.module';
import {SkisMainComponent} from './main/main.component';
import {ErrorComponent} from './shared/error/error.component';
import {LoginModalComponent} from './public/login/login.component';

@NgModule({
  declarations: [SkisMainComponent, ErrorComponent, LoginModalComponent],
  bootstrap: [SkisMainComponent],
  entryComponents: [LoginModalComponent],
  imports: [
    BrowserModule,
    SkispasseSharedModule,
    SkispasseCoreModule,
    WorldmapModule,
    SkispasseAppRoutingModule
  ]
})
export class SkispasseAppModule {
}
