import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {INewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {EventManager} from '../../core/events/event-manager';

@Component({
  selector: 'skis-delete-news-fact-dialog',
  templateUrl: './delete-news-fact-dialog.component.html'
})
export class DeleteNewsFactDialogComponent {
  newsFact: INewsFactDetail;

  constructor(private newsFactService: NewsFactService, public activeModal: NgbActiveModal, private eventManager: EventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete() {
    this.newsFactService.delete(this.newsFact.id).subscribe(() => {
      this.eventManager.broadcast({ name: 'myNewsFactListModification', content: 'Deleted a news fact' });
      this.activeModal.close(true);
    });
  }
}
