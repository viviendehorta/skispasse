import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ActivatedRoute, Router} from '@angular/router';

import {UserManagementDeleteDialogComponent} from './user-management-delete-dialog.component';
import {UserService} from '../../core/user/user.service';
import {AccountService} from '../../core/auth/account.service';
import {User} from '../../shared/model/user.model';
import {ITEMS_PER_PAGE} from '../../shared/constants/pagination.constants';
import {EventManager} from '../../core/events/event-manager';
import {PaginationService} from '../../core/pagination/pagination.service';
import {AlertService} from '../../core/alert/alert.service';
import {UserAccount} from "../../shared/model/account.model";

@Component({
  selector: 'skis-user-mgmt',
  templateUrl: './user-management.component.html'
})
export class UserManagementComponent implements OnInit, OnDestroy {
  currentAccount: UserAccount;
  users: User[];
  error: any;
  success: any;
  userListSubscription: Subscription;
  routeData: Subscription;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private accountService: AccountService,
    private paginationService: PaginationService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: EventManager,
    private modalService: NgbModal
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
    this.accountService.getAccount().subscribe(account => {
      this.currentAccount = account;
      this.loadAll();
      this.registerChangeInUsers();
    });
  }

  ngOnDestroy() {
    this.routeData.unsubscribe();
    if (this.userListSubscription) {
      this.eventManager.destroy(this.userListSubscription);
    }
  }

  registerChangeInUsers() {
    this.userListSubscription = this.eventManager.subscribe('userListModification', () => this.loadAll());
  }

  setActive(user, isActivated) {
    user.activated = isActivated;

    this.userService.update(user).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
        this.loadAll();
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  loadAll() {
    this.userService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers), (res: HttpResponse<any>) => this.onError(res.body));
  }

  trackIdentity(index, item: User) {
    return item.id;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
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

  deleteUser(user: User) {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
  }

  private onSuccess(data, headers) {
    this.links = this.paginationService.parseHeaderLinks(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.users = data;
  }

  private onError(error) {
    this.alertService.error(error.error, error.message);
  }
}
