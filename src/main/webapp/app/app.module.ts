import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SkispasseSharedModule } from 'app/shared/shared.module';
import { SkispasseCoreModule } from 'app/core/core.module';
import { SkispasseAppRoutingModule } from './app-routing.module';
import { SkispasseWorldmapModule } from './worldmap/worldmap.module';
import { SkispasseEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { SkisMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { NewsFactDetailModalContentComponent } from 'app/map/news-fact-detail-modal/news-fact-detail-modal.content.component';

@NgModule({
  declarations: [SkisMainComponent, NavbarComponent, ErrorComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [SkisMainComponent],
  entryComponents: [NewsFactDetailModalContentComponent],
  imports: [
    BrowserModule,
    SkispasseSharedModule,
    SkispasseCoreModule,
    SkispasseWorldmapModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SkispasseEntityModule,
    SkispasseAppRoutingModule
  ]
})
export class SkispasseAppModule {}
