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
import { NewsFactDetailModalContentComponent } from 'app/worldmap/news-fact-detail-modal/news-fact-detail-modal.content.component';
import { LoginModalComponent } from 'app/login/login.component';
import { NewsfactManagementComponent } from './contrib/newsfact-management/newsfact-management.component';

@NgModule({
  declarations: [SkisMainComponent, ErrorComponent, LoginModalComponent, NewsfactManagementComponent],
  bootstrap: [SkisMainComponent],
  entryComponents: [NewsFactDetailModalContentComponent, LoginModalComponent],
  imports: [
    BrowserModule,
    SkispasseSharedModule,
    SkispasseCoreModule,
    SkispasseWorldmapModule,
    SkispasseAppRoutingModule
    // jhipster-needle-angular-add-module JHipster will add new module here
  ]
})
export class SkispasseAppModule {}
