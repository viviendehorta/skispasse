import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from "rxjs";
import {EventManager} from "../../../core/events/event-manager";
import {UserService} from "../../../core/user/user.service";
import {IUser} from "../../../shared/model/user.model";

@Component({
    selector: 'skis-user-creation',
    templateUrl: './user-creation.component.html',
    styleUrls: ['user-creation.component.scss']
})
export class UserCreationComponent implements OnInit, OnDestroy {

    isCreating: boolean;
    userFormSubscription: Subscription;

    constructor(
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isCreating = false;
        this.userFormSubscription = this.eventManager.subscribe('userFormValidated', (event: { name: string; content: IUser }) => {
            this.userService
                .create(event.content)
                .subscribe(() => this.router.navigate(['/admin/user-management']),
                    () => {},
                    () =>  this.isCreating = false);
        });
    }

    ngOnDestroy(): void {
        this.userFormSubscription.unsubscribe();
    }
}
