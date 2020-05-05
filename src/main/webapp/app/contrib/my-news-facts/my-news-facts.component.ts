import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { INewsFactDetail } from 'app/shared/model/news-fact-detail.model';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { NewsFactService } from 'app/core/newsfacts/news-fact.service';
import { Account } from 'app/shared/model/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { ILocationCoordinate } from 'app/shared/model/location-coordinate.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteNewsFactDialogComponent } from 'app/contrib/my-news-facts/delete-news-fact-dialog.component';
import { INewsFactPage, NewsFactPage } from 'app/shared/model/news-fact-page.model';

@Component({
  selector: 'skis-news-fact-management',
  templateUrl: './my-news-facts.component.html',
  styleUrls: ['./my-news-facts.component.scss']
})
export class MyNewsFactsComponent implements OnInit {
  myNewsFacts: INewsFactDetail[];
  currentAccount: Account;
  routeData: Subscription;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  myNewsFactsSubscription: Subscription;

  constructor(
    private newsFactService: NewsFactService,
    private accountService: AccountService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private alertService: JhiAlertService,
    private modalService: NgbModal,
    private eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  ngOnInit() {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
      this.loadAll();
    });
    this.subscribeToChangesInMyNewsFactList();
  }

  subscribeToChangesInMyNewsFactList() {
    this.myNewsFactsSubscription = this.eventManager.subscribe('myNewsFactListModification', () => this.loadAll());
  }

  loadAll() {
    this.newsFactService
      .getByUser(this.currentAccount.login, {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((newsFactPage: NewsFactPage) => this.onSuccess(newsFactPage), (res: HttpResponse<any>) => this.onError(res.body));
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  transition() {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  trackIdentity(index, newsFactDetail: INewsFactDetail) {
    return newsFactDetail.id;
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  private onSuccess(newsFactPage: INewsFactPage) {
    this.links = newsFactPage.links;
    this.totalItems = newsFactPage.itemCount;
    this.myNewsFacts = newsFactPage.newsFactDetails;
  }

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }

  formatLocationCoordinate(locationCoordinate: ILocationCoordinate) {
    return '[' + locationCoordinate.x + '; ' + locationCoordinate.y + ']';
  }

  showDeleteNewsFactDialog(newsFactDetail: INewsFactDetail) {
    const modalRef = this.modalService.open(DeleteNewsFactDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.newsFact = newsFactDetail;
  }
}
