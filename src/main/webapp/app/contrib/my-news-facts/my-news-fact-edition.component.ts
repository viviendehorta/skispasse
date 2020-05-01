import { Component, OnInit } from '@angular/core';
import { NewsCategory } from 'app/shared/model/news-category.model';
import { NewsCategoryService } from 'app/core/newscategory/news-category.service';
import { FormBuilder, Validators } from '@angular/forms';
import { INewsFactDetail } from 'app/shared/model/news-fact-detail.model';
import { ActivatedRoute } from '@angular/router';
import { NewsFactService } from 'app/core/newsfacts/news-fact.service';

@Component({
  selector: 'skis-my-news-fact-edition',
  templateUrl: './my-news-fact-edition.component.html',
  styleUrls: ['my-news-fact-edition.component.scss']
})
export class MyNewsFactEditionComponent implements OnInit {
  newsFact: INewsFactDetail;
  newsCategories: NewsCategory[];

  isPublihing: boolean;

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
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isPublihing = false;

    this.route.data.subscribe(({ newsFact }) => {
      this.newsFact = newsFact;
      this.updateForm(this.newsFact);
    });

    this.newsCategoryService.fetchCategories().subscribe(unflattenedNewsCategories => {
      this.newsCategories = this.newsCategoryService.flattenNewsCategories(unflattenedNewsCategories);
    });
  }

  publish() {
    this.isPublihing = true;
    this.updateNewsFactFromForm();
    if (this.newsFact.id !== null) {
      this.newsFactService.update(this.newsFact).subscribe(() => this.onPublishSuccess(), () => this.onPublishError());
    } else {
      this.newsFactService.create(this.newsFact).subscribe(() => this.onPublishSuccess(), () => this.onPublishError());
    }
  }

  private updateForm(newsFact: INewsFactDetail): void {
    this.editionForm.patchValue({
      id: newsFact.id,
      newsCategoryId: newsFact.newsCategoryId,
      eventDate: newsFact.eventDate,
      address: newsFact.address,
      city: newsFact.city,
      country: newsFact.country,
      locationCoordinateX: newsFact.locationCoordinate.x,
      locationCoordinateY: newsFact.locationCoordinate.y,
      newsFactVideoPath: ''
    });
  }

  private updateNewsFactFromForm(): void {
    this.newsFact.address = this.editionForm.get(['address']).value;
    this.newsFact.city = this.editionForm.get(['city']).value;
    this.newsFact.country = this.editionForm.get(['country']).value;
    this.newsFact.eventDate = this.editionForm.get(['eventDate']).value;
    this.newsFact.id = this.editionForm.get(['id']).value;
    this.newsFact.locationCoordinate.x = this.editionForm.get(['locationCoordinateX']).value;
    this.newsFact.locationCoordinate.y = this.editionForm.get(['locationCoordinateY']).value;
    this.newsFact.newsCategoryId = this.editionForm.get(['newsCategoryId']).value;
    this.newsFact.videoPath = this.editionForm.get(['newsFactVideoPath']).value;
  }

  private onPublishSuccess() {
    this.isPublihing = false;
    this.previousState();
  }

  private onPublishError() {
    this.isPublihing = false;
  }

  previousState() {
    window.history.back();
  }
}
