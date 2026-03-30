import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {Button} from "primeng/button";
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
    ],
    providers: []
})
export class EventCreationComponent implements OnInit {
    creationForm: FormGroup;
    titleControl: FormControl<string>;
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
            value: "0"
        },
        {
            label: "Crise climatique",
            value: "1"
        },
        {
            label: "Délinquance financière",
            value: "2"
        },
        {
            label: "Droits humains",
            value: "3"
        },
        {
            label: "Santé publique",
            value: "4"
        },
        {
            label: "Secret défense",
            value: "5"
        },
        {
            label: "Sécurité alimentaire",
            value: "6"
        },
        {
            label: "Violences sexuelles",
            value: "7"
        },
        {
            label: "Évènementiel",
            value: "8"
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
                category: this.categoryControl.value!!,
                longitude: this.longitudeControl.value!!,
                latitude: this.latitudeControl.value!!
            }));
            this.router.navigate([""]);
        }
    }
}
