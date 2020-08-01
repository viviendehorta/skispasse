import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {EventManager} from "../../../core/events/event-manager";
import {Subscription} from "rxjs";
import {IUser} from "../../../shared/model/user.model";
import {UserService} from "../../../core/user/user.service";

@Component({
    selector: 'skis-user-edition',
    templateUrl: './user-edition.component.html'
})
export class UserEditionComponent implements OnInit, OnDestroy {
    user: IUser;
    isUpdating: boolean;
    userFormSubscription: Subscription;

    constructor(
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isUpdating = false;
        this.route.data.subscribe((data: { user: IUser }) => {
            this.user = data.user;
        });
        this.userFormSubscription = this.eventManager.subscribe('userFormValidated', (event: { name: string; content: IUser }) => {
            this.isUpdating = true;
            this.userService
                .update(event.content)
                .subscribe(() => this.router.navigate(['/admin/user-management']),
                    () => {},
                    () => this.isUpdating = false);
        });
    }

    ngOnDestroy(): void {
        this.userFormSubscription.unsubscribe();
    }
}
