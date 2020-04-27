import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SkispasseSharedModule } from 'app/shared/shared.module';
import { SkispasseCoreModule } from 'app/core/core.module';
import { SkispasseAppRoutingModule } from './app-routing.module';
import { WorldmapModule } from './public/worldmap/worldmap.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { SkisMainComponent } from './main/main.component';
import { ErrorComponent } from './shared/error/error.component';
import { NewsFactDetailModalContentComponent } from 'app/public/worldmap/news-fact-detail-modal/news-fact-detail-modal.content.component';
import { LoginModalComponent } from 'app/public/login/login.component';

@NgModule({
  declarations: [SkisMainComponent, ErrorComponent, LoginModalComponent],
  bootstrap: [SkisMainComponent],
  entryComponents: [NewsFactDetailModalContentComponent, LoginModalComponent],
  imports: [
    BrowserModule,
    SkispasseSharedModule,
    SkispasseCoreModule,
    WorldmapModule,
    SkispasseAppRoutingModule
    // jhipster-needle-angular-add-module JHipster will add new module here
  ]
})
export class SkispasseAppModule {}
