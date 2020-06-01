import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {AccountService} from '../../core/auth/account.service';
import {UserAccount} from '../../shared/model/account.model';
import {AuthenticationState} from "../../shared/model/authentication-state.model";
import { Subscription } from 'rxjs';

@Component({
  selector: 'skis-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit, OnDestroy {

  authSubscription: Subscription;

  error: string;
  success: string;
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [false],
    authorities: [[]],
    langKey: ['fr'],
    login: [],
    imageUrl: []
  });

  doesNotMatchPassword: string;

  passwordForm = this.fb.group({
    currentPassword: ['', [Validators.required]],
    newPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]]
  });

  constructor(private accountService: AccountService, private fb: FormBuilder) {}

  ngOnInit() {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe((authenticationState: AuthenticationState) => {
      this.updateSettingsForm(authenticationState.user);
    });
  }

  save() {
    const settingsAccount = this.accountFromForm();
    this.accountService.updateAccount(settingsAccount).subscribe(
      () => {
        this.error = null;
        this.success = 'Information was saved.';
        this.accountService.getAuthenticationState();
      },
      () => {
        this.success = null;
        this.error = 'Error while updating information!';
      }
    );
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  updateSettingsForm(account: UserAccount): void {
    this.settingsForm.patchValue({
      firstName: account.firstName,
      lastName: account.lastName,
      email: account.email,
      activated: account.activated,
      authorities: account.authorities,
      langKey: account.langKey,
      login: account.login,
      imageUrl: account.imageUrl
    });
  }

  changePassword() {
    const newPassword = this.passwordForm.get(['newPassword']).value;
    if (newPassword !== this.passwordForm.get(['confirmPassword']).value) {
      this.error = null;
      this.success = null;
      this.doesNotMatchPassword = 'The password and its confirmation do not match!';
    } else {
      this.doesNotMatchPassword = null;
      this.accountService.updatePassword(newPassword, this.passwordForm.get(['currentPassword']).value).subscribe(
          () => {
            this.error = null;
            this.success = 'Password was successfully updated.';
          },
          () => {
            this.success = null;
            this.error = 'Error while updating password!';
          }
      );
    }
  }

  private accountFromForm(): any {
    const account = {};
    return {
      ...account,
      firstName: this.settingsForm.get('firstName').value,
      lastName: this.settingsForm.get('lastName').value,
      email: this.settingsForm.get('email').value,
      activated: this.settingsForm.get('activated').value,
      authorities: this.settingsForm.get('authorities').value,
      langKey: this.settingsForm.get('langKey').value,
      login: this.settingsForm.get('login').value,
      imageUrl: this.settingsForm.get('imageUrl').value
    };
  }
}
