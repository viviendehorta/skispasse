import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NewsFactService} from '../../../core/newsfacts/news-fact.service';
import {Subscription} from "rxjs";
import {INewsFactWithFile} from "../news-fact-form/news-fact-with-file.model";
import {EventManager} from "../../../core/events/event-manager";

@Component({
    selector: 'skis-news-fact-creation',
    templateUrl: './news-fact-creation.component.html',
    styleUrls: ['news-fact-creation.component.scss']
})
export class NewsFactCreationComponent implements OnInit, OnDestroy {

    isCreating: boolean;
    newsFactFormSubscription: Subscription;

    constructor(
        private newsFactService: NewsFactService,
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isCreating = false;
        this.newsFactFormSubscription = this.eventManager.subscribe('newsFactFormValidated', (event: { name: string; content: INewsFactWithFile }) => {
            this.newsFactService
                .create(event.content)
                .subscribe(() => this.router.navigate(['/contrib/my-news-facts']),
                    () => {},
                    () =>  this.isCreating = false);
        });
    }

    ngOnDestroy(): void {
        this.newsFactFormSubscription.unsubscribe();
    }
}
