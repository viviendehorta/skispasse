import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/shared/model/account.model';
import { PasswordService } from 'app/core/account/password.service';

@Component({
  selector: 'skis-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  errorI18NKey: string;
  successI18NKey: string;
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

  constructor(private accountService: AccountService, private fb: FormBuilder, private passwordService: PasswordService) {}

  ngOnInit() {
    this.accountService.identity().subscribe(account => {
      this.updateSettingsForm(account);
    });
  }

  save() {
    const settingsAccount = this.accountFromForm();
    this.accountService.save(settingsAccount).subscribe(
      () => {
        this.errorI18NKey = null;
        this.successI18NKey = 'settings';
        this.accountService.identity(true).subscribe(account => {
          this.updateSettingsForm(account);
        });
      },
      () => {
        this.successI18NKey = null;
        this.errorI18NKey = 'settings';
      }
    );
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

  updateSettingsForm(account: Account): void {
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
      this.errorI18NKey = null;
      this.successI18NKey = null;
      this.doesNotMatchPassword = 'ERROR';
    } else {
      this.doesNotMatchPassword = null;
      this.passwordService.save(newPassword, this.passwordForm.get(['currentPassword']).value).subscribe(
        () => {
          this.errorI18NKey = null;
          this.successI18NKey = 'password';
        },
        () => {
          this.successI18NKey = null;
          this.errorI18NKey = 'password';
        }
      );
    }
  }
}
