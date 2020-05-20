import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {UserService} from '../../core/user/user.service';
import {User} from '../../shared/model/user.model';
import {EventManager} from '../../core/events/event-manager';


@Component({
  selector: 'skis-user-mgmt-delete-dialog',
  templateUrl: './user-management-delete-dialog.component.html'
})
export class UserManagementDeleteDialogComponent {
  user: User;

  constructor(private userService: UserService, public activeModal: NgbActiveModal, private eventManager: EventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(login) {
    this.userService.delete(login).subscribe(() => {
      this.eventManager.broadcast({ name: 'userListModification', content: 'Deleted a user' });
      this.activeModal.close(true);
    });
  }
}
