import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  templateUrl: './news-fact-detail-modal-content.component.html',
  styleUrls: ['./news-fact-detail-modal.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NewsFactDetailModalContentComponent implements OnInit {
  @Input() newsFactDetail: { address: string; city: string; country: string };

  constructor() {}

  ngOnInit() {}

  setNewsFactDetail(newsFactDetail: { address: string; city: string; country: string }) {
    this.newsFactDetail = newsFactDetail;
  }
}
