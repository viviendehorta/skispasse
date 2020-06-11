import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {INewsFactDetail} from '../../../shared/model/news-fact-detail.model';
import {NewsFactService} from '../../../core/newsfacts/news-fact.service';
import {EventManager} from "../../../core/events/event-manager";
import {Subscription} from "rxjs";
import {INewsFactWithFile} from "../news-fact-form/news-fact-with-file.model";

@Component({
    selector: 'skis-news-fact-edition',
    templateUrl: './news-fact-edition.component.html',
    styleUrls: ['news-fact-edition.component.scss']
})
export class NewsFactEditionComponent implements OnInit, OnDestroy {
    newsFact: INewsFactDetail;
    isUpdating: boolean;
    newsFactFormSubscription: Subscription;

    constructor(
        private newsFactService: NewsFactService,
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isUpdating = false;
        this.route.data.subscribe((data: { newsFact: INewsFactDetail }) => {
            this.newsFact = data.newsFact;
        });
        this.newsFactFormSubscription = this.eventManager.subscribe('newsFactFormValidated', (event: { name: string; content: INewsFactWithFile }) => {
            this.isUpdating = true;
            this.newsFactService
                .update(event.content.newsFact)
                .subscribe(() => this.router.navigate(['/contrib/my-news-facts']),
                    () => {},
                    () => this.isUpdating = false);
        });
    }

    ngOnDestroy(): void {
        this.newsFactFormSubscription.unsubscribe();
    }
}
