import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {EventManager} from "../../../core/events/event-manager";
import {User} from "../../../shared/model/user.model";
import {UserService} from "../../../core/user/user.service";
import {Location} from "@angular/common";

@Component({
    selector: 'skis-user-form',
    templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {

    @Input() user: User;

    authorities: String[];
    userForm: FormGroup;

    constructor(
        private userService: UserService,
        private fb: FormBuilder,
        private eventManager: EventManager,
        private location: Location
    ) {
    }

    ngOnInit() {
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.userForm = this.fb.group({
            id: [null],
            login: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*')]],
            firstName: [null, [Validators.maxLength(50)]],
            lastName: [null, [Validators.maxLength(50)]],
            email: [null, [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
            activated: [true],
            langKey: [],
            authorities: []
        });
        if (this.user) {
            this.updateForm(this.user);
        }
    }

    submit(): void {
        this.eventManager.broadcast({
            name: 'userFormValidated',
            content: this.getUserFromForm()
        });
    }

    goBack() {
        this.location.back();
    }

    private updateForm(user: User): void {
        this.userForm.patchValue({
            id: user.id,
            login: user.login,
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            activated: user.activated,
            langKey: user.langKey,
            authorities: user.authorities
        });
    }

    private getUserFromForm(): User {
        return new User(
            this.user ? this.user.id : null,
            this.userForm.get(['login']).value,
            this.userForm.get(['firstName']).value,
            this.userForm.get(['lastName']).value,
            this.userForm.get(['email']).value,
            this.userForm.get(['activated']).value,
            'en',
            this.userForm.get(['authorities']).value
        );
    }
}
