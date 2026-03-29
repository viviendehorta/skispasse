import {ApplicationConfig, provideZoneChangeDetection} from "@angular/core";
import {provideRouter} from "@angular/router";

import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {providePrimeNG} from "primeng/config";
import {provideHttpClient} from "@angular/common/http";
import {ConfirmationService, MessageService} from "primeng/api";
import {routes} from "./routes";
import {primengPreset} from "../style/primeng-preset";
import {provideState, provideStore} from '@ngrx/store';
import {mapEffects, mapFeature} from "./core/map-store";
import {provideStoreDevtools} from "@ngrx/store-devtools";
import {provideEffects} from "@ngrx/effects";
import {EventService} from "./core/service/event.service";

export const appConfig: ApplicationConfig = {
    providers: [
        provideZoneChangeDetection({eventCoalescing: true}),
        provideRouter(routes),
        provideAnimationsAsync(),
        providePrimeNG({
            theme: {
                preset: primengPreset,
            },
            translation: {
                dayNamesMin: ["Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"],
                monthNames: ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"],
                monthNamesShort: ["jan", "fév", "mars", "avr", "mai", "juin", "juil", "août", "sept", "oct", "nov", "déc"],
            },
        }),
        provideHttpClient(),
        provideStore(),
        provideState(mapFeature),
        provideEffects([mapEffects]),
        provideStoreDevtools(),
        ConfirmationService,
        EventService,
        MessageService
    ]
};
