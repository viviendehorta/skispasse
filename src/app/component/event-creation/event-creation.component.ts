import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {Button} from "primeng/button";
import {FileSelectEvent, FileUpload} from "primeng/fileupload";
import {InputText} from "primeng/inputtext";
import {Select} from "primeng/select";
import {mapActions, mapFeature, MapState} from "../../core/map-store";

@Component({
    templateUrl: "./event-creation.component.html",
    styleUrls: ["event-creation.component.scss"],
    standalone: true,
    imports: [
        ReactiveFormsModule,
        InputText,
        Button,
        Select,
        FileUpload,
    ],
    providers: []
})
export class EventCreationComponent implements OnInit {
    creationForm: FormGroup;
    titleControl: FormControl<string>;
    cityControl: FormControl<string>;
    countryControl: FormControl<string>;
    categoryControl: FormControl<string | null>;
    latitudeControl: FormControl<number | null>;
    longitudeControl: FormControl<number | null>;
    categoryValues: { label: string, value: string | null }[] = [
        {
            label: "-",
            value: null
        },
        {
            label: "Crime environnemental",
            value: "CRIME_ENVIRONNEMENTAL"
        },
        {
            label: "Crise climatique",
            value: "CRISE_CLIMATIQUE"
        },
        {
            label: "Délinquance financière",
            value: "DELINQUANCE_FINANCIERE"
        },
        {
            label: "Droits humains",
            value: "DROITS_HUMAINS"
        },
        {
            label: "Santé publique",
            value: "SANTE_PUBLIQUE"
        },
        {
            label: "Secret défense",
            value: "SECRET_DEFENSE"
        },
        {
            label: "Sécurité alimentaire",
            value: "SECURITE_ALIMENTAIRE"
        },
        {
            label: "Violences sexuelles",
            value: "VIOLENCES_SEXUELLES"
        },
        {
            label: "Évènementiel",
            value: "EVENEMENTIEL"
        }
    ];

    constructor(
        private fb: FormBuilder,
        private mapStore: Store<MapState>,
        private router: Router
    ) {
    }

    ngOnInit(): void {
        this.mapStore.select(mapFeature.selectLocationForEventCreation).subscribe(location => {
            this.titleControl = new FormControl<string>("", {
                nonNullable: true,
                validators: Validators.required
            });
            this.cityControl = new FormControl<string>("", {
                nonNullable: true,
                validators: Validators.required
            });
            this.countryControl = new FormControl<string>("", {
                nonNullable: true,
                validators: Validators.required
            });
            this.categoryControl = new FormControl<string | null>(null, {
                validators: Validators.required
            });
            this.latitudeControl = new FormControl<number | null>(location?.latitude || null, {
                validators: Validators.required
            });
            this.longitudeControl = new FormControl<number | null>(location?.longitude || null, {
                validators: Validators.required
            });
            this.creationForm = this.fb.group([this.titleControl]);
        });
    }

    protected onSubmit() {
        if (this.creationForm.valid) {
            this.mapStore.dispatch(mapActions.addEvent({
                title: this.titleControl.value,
                city: this.cityControl.value,
                country: this.countryControl.value,
                category: this.categoryControl.value!!,
                longitude: this.longitudeControl.value!!,
                latitude: this.latitudeControl.value!!
            }));
            this.router.navigate([""]);
        }
    }

    protected onSelect(fileSelectEvent: FileSelectEvent) {
        console.log(JSON.stringify(fileSelectEvent));
    }
}
