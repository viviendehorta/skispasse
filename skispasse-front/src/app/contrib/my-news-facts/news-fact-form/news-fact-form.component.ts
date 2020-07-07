import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NewsCategoryService} from '../../../core/newscategory/news-category.service';
import {INewsFactDetail, NewsFactDetail} from '../../../shared/model/news-fact-detail.model';
import {NewsCategory} from '../../../shared/model/news-category.model';
import {NewsFactService} from '../../../core/newsfacts/news-fact.service';
import * as  dayjs from 'dayjs';
import {LocationCoordinate} from "../../../shared/model/location-coordinate.model";
import {EventManager} from "../../../core/events/event-manager";
import {NewsFactWithFile} from "./news-fact-with-file.model";
import {Location} from "@angular/common";

@Component({
    selector: 'skis-news-fact-form',
    templateUrl: './news-fact-form.component.html'
})
export class NewsFactFormComponent implements OnInit {

    @Input() withFileUpload: boolean;
    @Input() newsFact: INewsFactDetail;

    file: File | null;
    newsCategories: NewsCategory[];
    newsFactForm: FormGroup;

    constructor(
        private newsCategoryService: NewsCategoryService,
        private newsFactService: NewsFactService,
        private fb: FormBuilder,
        private eventManager: EventManager,
        private location: Location
    ) {
    }

    ngOnInit() {
        this.file = null;
        this.newsCategoryService.fetchCategories().subscribe(unflattenedNewsCategories => {
            this.newsCategories = this.newsCategoryService.flattenNewsCategories(unflattenedNewsCategories);
        });
        this.newsFactForm = this.fb.group({
            id: [null],
            newsCategoryId: [null, Validators.required],
            eventDate: [null, Validators.required],
            address: [null, Validators.required],
            city: [null, Validators.required],
            country: [null, Validators.required],
            locationCoordinateX: [null, Validators.required],
            locationCoordinateY: [null, Validators.required],
            newsFactMediaFile: this.withFileUpload ? [null, Validators.required] : null
        });
        if (this.newsFact) {
            this.updateForm(this.newsFact);
        }
    }

    submit(): void {
        this.eventManager.broadcast({
            name: 'newsFactFormValidated',
            content: new NewsFactWithFile(this.getNewsFactFromForm(), this.file)
        });
    }

    onFileChange(event) {
        if (event.target.files.length > 0) {
            this.file = event.target.files[0];
        }
    }

    goBack() {
        this.location.back();
    }

    private updateForm(newsFact: INewsFactDetail): void {
        this.newsFactForm.patchValue({
            id: newsFact.id,
            newsCategoryId: newsFact.newsCategoryId,
            eventDate: NewsFactFormComponent.parseDate(newsFact.eventDate),
            address: newsFact.address,
            city: newsFact.city,
            country: newsFact.country,
            locationCoordinateX: newsFact.locationCoordinate.x,
            locationCoordinateY: newsFact.locationCoordinate.y,
            newsFactMediaFile: null
        });
    }

    private getNewsFactFromForm(): INewsFactDetail {
        return new NewsFactDetail(
            this.newsFactForm.get(['address']).value,
            this.newsFactForm.get(['city']).value,
            this.newsFactForm.get(['country']).value,
            null,
            NewsFactFormComponent.formatNgbDate(this.newsFactForm.get(['eventDate']).value),
            this.newsFact ? this.newsFact.id : null,
            new LocationCoordinate(
                this.newsFactForm.get(['locationCoordinateX']).value,
                this.newsFactForm.get(['locationCoordinateY']).value
            ),
            this.newsFactForm.get(['newsCategoryId']).value,
            null,
            null
        );
    }

    private static formatNgbDate(ngbDate: Date): string {
        return dayjs(ngbDate).format('YYYY-MM-DD');
    }

    private static parseDate(dateString: string): Date {
        return dayjs(dateString).toDate();
    }
}
