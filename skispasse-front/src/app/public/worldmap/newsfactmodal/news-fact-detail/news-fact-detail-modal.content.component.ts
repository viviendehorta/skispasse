import {Component, OnInit} from '@angular/core';
import {INewsFactDetail} from "../../../../shared/model/news-fact-detail.model";
import {NewsFactService} from "../../../../core/newsfacts/news-fact.service";
import {HTML_COLOR_BY_NEWS_CATEGORY} from "../../../../core/map/news-category-color.constants";
import {ActivatedRoute} from "@angular/router";

@Component({
    templateUrl: './news-fact-detail-modal-content.component.html'
})
export class NewsFactDetailModalContentComponent implements OnInit {

    newsFactDetail: INewsFactDetail;
    newsFactMediaData: string;

    constructor(
        private newsFactService: NewsFactService,
        private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.data.subscribe((data) => {
            this.newsFactDetail = data.newsFact;
            if (this.newsFactDetail.media.type === 'VIDEO') {
                this.newsFactMediaData = this.newsFactService.getNewsFactVideoUrl(this.newsFactDetail.id);
            } else {
                this.newsFactMediaData = `data:${this.newsFactDetail.media.contentType};base64,${this.newsFactService.getNewsFactImageString(this.newsFactDetail.id)}`;
            }
        });
    }

    getNewsCategoryColor(): string {
        return HTML_COLOR_BY_NEWS_CATEGORY[this.newsFactDetail.newsCategoryId];
    }
}
