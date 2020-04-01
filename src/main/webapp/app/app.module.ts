import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SkispasseSharedModule } from 'app/shared/shared.module';
import { SkispasseCoreModule } from 'app/core/core.module';
import { SkispasseAppRoutingModule } from './app-routing.module';
import { SkispasseWorldmapModule } from './worldmap/worldmap.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { SkisMainComponent } from './layouts/main/main.component';
import { ErrorComponent } from './layouts/error/error.component';
import { NewsFactDetailModalContentComponent } from 'app/map/news-fact-detail-modal/news-fact-detail-modal.content.component';

@NgModule({
  declarations: [SkisMainComponent, ErrorComponent],
  bootstrap: [SkisMainComponent],
  entryComponents: [NewsFactDetailModalContentComponent],
  imports: [
    BrowserModule,
    SkispasseSharedModule,
    SkispasseCoreModule,
    SkispasseWorldmapModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SkispasseAppRoutingModule
  ]
})
export class SkispasseAppModule {}
