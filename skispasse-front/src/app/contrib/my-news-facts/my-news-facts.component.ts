import {Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {INewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {BASIC_PAGE_ITEM_COUNT} from '../../shared/constants/pagination.constants';
import {UserAccount} from '../../shared/model/account.model';
import {ILocationCoordinate} from '../../shared/model/location-coordinate.model';
import {DeleteNewsFactDialogComponent} from './delete-news-fact-dialog/delete-news-fact-dialog.component';
import {AccountService} from '../../core/auth/account.service';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {EventManager} from '../../core/events/event-manager';
import {AlertService} from '../../core/alert/alert.service';
import {HTML_COLOR_BY_NEWS_CATEGORY} from "../../core/map/news-category-color.constants";
import {Page} from "../../shared/model/page.model";

@Component({
    selector: 'skis-news-fact-management',
    templateUrl: './my-news-facts.component.html'
})
export class MyNewsFactsComponent implements OnInit {

    myNewsFacts: INewsFactDetail[];
    currentAccount: UserAccount;
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
        private alertService: AlertService,
        private modalService: NgbModal,
        private eventManager: EventManager
    ) {
        this.itemsPerPage = BASIC_PAGE_ITEM_COUNT;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    ngOnInit() {
        this.accountService.getAccount().subscribe((account) => {
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
            .subscribe((newsFactPage: Page) => this.onSuccess(newsFactPage), (res: HttpResponse<any>) => this.onError(res.body));
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

    private onSuccess(newsFactPage: Page) {
        this.links = newsFactPage.links;
        this.totalItems = newsFactPage.itemCount;
        this.myNewsFacts = newsFactPage.items;
    }

    private onError(error) {
        this.alertService.error(error.error, error.message);
    }

    formatLocationCoordinate(locationCoordinate: ILocationCoordinate) {
        return '[' + locationCoordinate.x + '; ' + locationCoordinate.y + ']';
    }

    showDeleteNewsFactDialog(newsFactDetail: INewsFactDetail) {
        const modalRef = this.modalService.open(DeleteNewsFactDialogComponent, {size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.newsFact = newsFactDetail;
    }

    getNewsCategoryColor(newsCategoryId: string) {
        return HTML_COLOR_BY_NEWS_CATEGORY[newsCategoryId];
    }
}
