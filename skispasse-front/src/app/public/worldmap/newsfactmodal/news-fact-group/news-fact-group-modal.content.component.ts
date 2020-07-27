import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {NewsFactNoDetail} from "../../../../shared/model/news-fact-no-detail.model";
import {NewsCategory} from "../../../../shared/model/news-category.model";
import {NewsCategoryService} from "../../../../core/newscategory/news-category.service";
import {HTML_COLOR_BY_NEWS_CATEGORY} from "../../../../core/map/news-category-color.constants";
import {ILocationCoordinate} from "../../../../shared/model/location-coordinate.model";

@Component({
    templateUrl: './news-fact-group-modal-content.component.html',
    styleUrls: ['./news-fact-group-modal-content.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class NewsFactGroupModalContentComponent implements OnInit {

    newsFactNoDetails: NewsFactNoDetail[];
    allNewsCategories: NewsCategory[];

    constructor(private newsCategoryService: NewsCategoryService) {
    }

    ngOnInit() {
        this.newsCategoryService.fetchNewsCategories().subscribe(newsCategories => this.allNewsCategories = newsCategories);
    }

    getNewsCategoryColor(newsFactNoDetail: NewsFactNoDetail):string {
        return HTML_COLOR_BY_NEWS_CATEGORY[newsFactNoDetail.newsCategoryId];
    }

    setNewsFactNoDetails(newsFactNoDetails: NewsFactNoDetail[]) {
        this.newsFactNoDetails = newsFactNoDetails;
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
