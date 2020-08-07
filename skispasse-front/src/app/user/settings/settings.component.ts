import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {AccountService} from '../../core/auth/account.service';
import {UserAccount} from '../../shared/model/account.model';

@Component({
  selector: 'skis-settings',
  templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {

  error: string;
  success: string;
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [false],
    authorities: [[]],
    login: []
  });

  doesNotMatchPassword: string;

  passwordForm = this.fb.group({
    currentPassword: ['', [Validators.required]],
    newPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]]
  });

  constructor(private accountService: AccountService, private fb: FormBuilder) {}

  ngOnInit() {
    this.accountService.getAccount().subscribe((account) => {
      this.updateSettingsForm(account);
    });
  }

  save() {
    const updatedAccount = this.accountFromForm();
    this.accountService.updateAccount(updatedAccount).subscribe(
      () => {
        this.error = null;
        this.success = 'Information was saved.';
        this.accountService.authenticate(updatedAccount);
      },
      () => {
        this.success = null;
        this.error = 'Error while updating account information!';
      }
    );
  }

  updateSettingsForm(account: UserAccount): void {
    this.settingsForm.patchValue({
      firstName: account.firstName,
      lastName: account.lastName,
      email: account.email,
      activated: account.activated,
      authorities: account.authorities,
      login: account.login,
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
      login: this.settingsForm.get('login').value
    };
  }
}
