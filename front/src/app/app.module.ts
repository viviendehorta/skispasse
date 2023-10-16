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

@NgModule({
    declarations: [
        AppComponent,
        MenuComponent,
        MenuLogoComponent,
        NewsfactsMapComponent,
        WorldViewComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ButtonModule,
        HttpClientModule,
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
