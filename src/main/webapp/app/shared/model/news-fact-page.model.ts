import { NewsFactDetail } from 'app/shared/model/news-fact-detail.model';

export interface INewsFactPage {
  newsFactDetails: NewsFactDetail[];
  itemCount: any;
  links: any;
}

export class NewsFactPage implements INewsFactPage {
  newsFactDetails: NewsFactDetail[];
  itemCount: any;
  links: any;

  constructor(newsFactDetails: NewsFactDetail[], itemCount: any, links: any) {
    this.newsFactDetails = newsFactDetails;
    this.itemCount = itemCount;
    this.links = links;
  }
}
