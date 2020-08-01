import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {NewsFactNoDetail} from "../../../../shared/model/news-fact-no-detail.model";
import {NewsCategory} from "../../../../shared/model/news-category.model";
import {NewsCategoryService} from "../../../../core/newscategory/news-category.service";
import {HTML_COLOR_BY_NEWS_CATEGORY} from "../../../../core/map/news-category-color.constants";
import {ILocationCoordinate} from "../../../../shared/model/location-coordinate.model";
import {ActivatedRoute} from "@angular/router";

@Component({
    templateUrl: './news-fact-group-modal-content.component.html'
})
export class NewsFactGroupModalContentComponent implements OnInit {

    newsFactNoDetails: NewsFactNoDetail[];
    allNewsCategories: NewsCategory[];

    constructor(
        private newsCategoryService: NewsCategoryService,
        private route: ActivatedRoute
        ) {
    }

    ngOnInit() {
        this.route.data.subscribe((data) => {
            this.newsFactNoDetails = data.newsFacts;
            this.newsCategoryService.fetchNewsCategories().subscribe(newsCategories => {
                return this.allNewsCategories = newsCategories;
            });
        });
    }

    getNewsCategoryColor(newsFactNoDetail: NewsFactNoDetail):string {
        return HTML_COLOR_BY_NEWS_CATEGORY[newsFactNoDetail.newsCategoryId];
    }

    trackIdentity(index, newsFactNoDetail: NewsFactNoDetail) {
        return newsFactNoDetail.id;
    }

    getNewsCategoryLabel(newsCategoryId: string): string {
        return this.allNewsCategories.find(newsCategory => newsCategory.id === newsCategoryId).label;
    }

    formatLocationCoordinate(locationCoordinate: ILocationCoordinate) {
        return '[' + locationCoordinate.x + '; ' + locationCoordinate.y + ']';
    }
}
