import { Component, OnInit } from '@angular/core';
import { NewsCategory } from 'app/shared/model/news-category.model';
import { NewsCategoryService } from 'app/core/newscategory/news-category.service';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'skis-my-news-fact-edition',
  templateUrl: './my-news-fact-edition.component.html'
})
export class MyNewsFactEditionComponent implements OnInit {
  newsCategories: NewsCategory[];
  eventDateModel: NgbDateStruct;

  constructor(private newsCategoryService: NewsCategoryService) {}

  ngOnInit() {
    this.newsCategoryService.fetchCategories().subscribe(unflattenedNewsCategories => {
      const flattenedNewsCategories = this.newsCategoryService.flattenNewsCategories(unflattenedNewsCategories);
      this.newsCategories = flattenedNewsCategories;
    });
  }
}
