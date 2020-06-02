import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRouteSnapshot, NavigationEnd, NavigationError, Router} from '@angular/router';
import {Title} from "@angular/platform-browser";
import {AccountService} from "../core/auth/account.service";
import {Subscription} from "rxjs";


@Component({
  selector: 'skis-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.scss']
})
export class SkisMainComponent implements OnInit, OnDestroy {

  authSubscription: Subscription; //Own the app authentication subscription (no need to manipulate it in other components)

  constructor(private accountService: AccountService, private titleService: Title, private router: Router) {}

  ngOnInit() {
    // try to log in automatically
    this.authSubscription = this.accountService.getAccount().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'Skispasse';
    }
    this.titleService.setTitle(pageTitle);
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title: string = routeSnapshot.data && routeSnapshot.data.pageTitle ? routeSnapshot.data.pageTitle : 'skispasseApp';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }
}
