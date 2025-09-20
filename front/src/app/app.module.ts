import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NewsfactsMapComponent} from "./world-view/newsfacts-map/newsfacts-map.component";
import {ButtonModule} from "primeng/button";
import {WorldViewComponent} from "./world-view/world-view.component"
import {MenuComponent} from "./menu/menu.component"
import {MenuLogoComponent} from "./menu/menu-logo/menu-logo.component"
import {HttpClientModule} from "@angular/common/http"
import {NewsfactViewComponent} from "./world-view/newsfact-view/newsfact-view.component"
import {DialogModule} from "primeng/dialog"
import {BrowserAnimationsModule} from "@angular/platform-browser/animations"
import {ConfirmDialogModule} from "primeng/confirmdialog"
import {NewsFactListComponent} from "./world-view/newsfact-list/news-fact-list.component"
import {CommonModule} from "@angular/common"

@NgModule({
    declarations: [
        AppComponent,
        MenuComponent,
        MenuLogoComponent,
        NewsFactListComponent,
        NewsfactsMapComponent,
        NewsfactViewComponent,
        WorldViewComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ButtonModule,
        ConfirmDialogModule,
        DialogModule,
        HttpClientModule,
        BrowserAnimationsModule,
        CommonModule,
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
