import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {INewsFactDetail} from "../../../shared/model/news-fact-detail.model";
import {NewsFactService} from "../../../core/newsfacts/news-fact.service";

@Component({
    templateUrl: './news-fact-detail-modal-content.component.html',
    styleUrls: ['./news-fact-detail-modal-content.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class NewsFactDetailModalContentComponent implements OnInit {

    @Input() newsFactDetail: INewsFactDetail;
    newsFactVideoUrl: string;

    constructor(private newsFactService: NewsFactService) {
    }

    ngOnInit() {
        this.newsFactVideoUrl = this.newsFactService.getNewsFactVideoUrl(this.newsFactDetail.id);
    }

    setNewsFactDetail(newsFactDetail: INewsFactDetail) {
        this.newsFactDetail = newsFactDetail;
    }
}
