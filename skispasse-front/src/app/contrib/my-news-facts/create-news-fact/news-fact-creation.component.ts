import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NewsFactService} from '../../../core/newsfacts/news-fact.service';
import {Subscription} from "rxjs";
import {INewsFactWithFile} from "../news-fact-form/news-fact-with-file.model";
import {EventManager} from "../../../core/events/event-manager";
import {INewsFactDetail, NewsFactDetail} from "../../../shared/model/news-fact-detail.model";
import {INewsFactLocation} from "../../../shared/model/alert/news-fact-location.model";

@Component({
    selector: 'skis-news-fact-creation',
    templateUrl: './news-fact-creation.component.html'
})
export class NewsFactCreationComponent implements OnInit, OnDestroy {

    isCreating: boolean;
    newsFactFormSubscription: Subscription;
    toCreateNewsFact: INewsFactDetail;

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
                    () => {
                    },
                    () => this.isCreating = false);
        });

        this.route.data.subscribe((data: { newsFactLocation: INewsFactLocation }) => {
            const newsFactLocation = data.newsFactLocation;
            this.toCreateNewsFact = new NewsFactDetail(
                newsFactLocation.address.detail,
                newsFactLocation.address.city,
                newsFactLocation.address.country,
                null,
                null,
                null,
                newsFactLocation.locationCoordinate);
        });
    }

    ngOnDestroy(): void {
        this.newsFactFormSubscription.unsubscribe();
    }
}
