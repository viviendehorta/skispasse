import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NewsCategoryService} from '../../core/newscategory/news-category.service';
import {INewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {NewsCategory} from '../../shared/model/news-category.model';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import * as  dayjs from 'dayjs';

@Component({
    selector: 'skis-my-news-fact-edition',
    templateUrl: './my-news-fact-edition.component.html',
    styleUrls: ['my-news-fact-edition.component.scss']
})
export class MyNewsFactEditionComponent implements OnInit {
    newsFact: INewsFactDetail;
    newsFactVideoFile: File;
    newsCategories: NewsCategory[];

    isPublishing: boolean;

    editionForm = this.fb.group({
        id: [null],
        newsCategoryId: [null, Validators.required],
        eventDate: [null, Validators.required],
        address: [null, Validators.required],
        city: [null, Validators.required],
        country: [null, Validators.required],
        locationCoordinateX: [null, Validators.required],
        locationCoordinateY: [null, Validators.required],
        newsFactVideoPath: ['']
    });

    constructor(
        private newsCategoryService: NewsCategoryService,
        private newsFactService: NewsFactService,
        private route: ActivatedRoute,
        private fb: FormBuilder,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.isPublishing = false;
        this.newsFactVideoFile = null;

        this.route.data.subscribe(({newsFact}) => {
            this.newsFact = newsFact;
            this.updateForm(this.newsFact);
        });

        this.newsCategoryService.fetchCategories().subscribe(unflattenedNewsCategories => {
            this.newsCategories = this.newsCategoryService.flattenNewsCategories(unflattenedNewsCategories);
        });
    }

    publish() {
        this.isPublishing = true;
        this.updateNewsFactFromForm();
        if (this.newsFact.id !== null) {
            this.newsFactService
                .update(this.newsFact)
                .subscribe((updatedNewsFact: INewsFactDetail) => this.onPublishSuccess(updatedNewsFact), () => this.onPublishError());
        } else {
            this.newsFactService
                .create(this.newsFact, this.newsFactVideoFile)
                .subscribe((createdNewsFact: INewsFactDetail) => this.onPublishSuccess(createdNewsFact), () => this.onPublishError());
        }
    }

    onFileChange(event) {
        if (event.target.files.length > 0) {
            this.newsFactVideoFile = event.target.files[0];
        }
    }

    previousState() {
        window.history.back();
    }

    private updateForm(newsFact: INewsFactDetail): void {
        this.editionForm.patchValue({
            id: newsFact.id,
            newsCategoryId: newsFact.newsCategoryId,
            eventDate: this.parseDate(newsFact.eventDate),
            address: newsFact.address,
            city: newsFact.city,
            country: newsFact.country,
            locationCoordinateX: newsFact.locationCoordinate.x,
            locationCoordinateY: newsFact.locationCoordinate.y,
            newsFactVideoPath: null
        });
    }

    private updateNewsFactFromForm(): void {
        this.newsFact.address = this.editionForm.get(['address']).value;
        this.newsFact.city = this.editionForm.get(['city']).value;
        this.newsFact.country = this.editionForm.get(['country']).value;
        this.newsFact.eventDate = this.formatNgbDate(this.editionForm.get(['eventDate']).value);
        this.newsFact.id = this.editionForm.get(['id']).value;
        this.newsFact.locationCoordinate.x = this.editionForm.get(['locationCoordinateX']).value;
        this.newsFact.locationCoordinate.y = this.editionForm.get(['locationCoordinateY']).value;
        this.newsFact.newsCategoryId = this.editionForm.get(['newsCategoryId']).value;

        // Upload input value is fake, we use form and multipart to upload => set newsFact.videoPath to null
        this.newsFact.videoPath = null;
    }

    private onPublishSuccess(newsFact: INewsFactDetail) {
        this.router.navigate(['/contrib/my-news-facts']);
    }

    private onPublishError() {
        this.isPublishing = false;
    }

    private formatNgbDate(ngbDate: Date): string {
        return dayjs(ngbDate).format('YYYY-MM-DD');
    }

    private parseDate(dateString: string): Date {
        return dayjs(dateString).toDate();
    }
}
