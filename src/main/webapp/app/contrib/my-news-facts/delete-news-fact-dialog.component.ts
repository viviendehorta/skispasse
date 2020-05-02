import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { INewsFactDetail } from 'app/shared/model/news-fact-detail.model';
import { NewsFactService } from 'app/core/newsfacts/news-fact.service';

@Component({
  selector: 'skis-delete-news-fact-dialog',
  templateUrl: './delete-news-fact-dialog.component.html'
})
export class DeleteNewsFactDialogComponent {
  newsFact: INewsFactDetail;

  constructor(private newsFactService: NewsFactService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

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
