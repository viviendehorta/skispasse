import {AfterViewInit, Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import Map from 'ol/Map';
import {Vector as VectorLayer} from 'ol/layer';
import {Feature} from 'ol';
import {Subscription} from 'rxjs';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {AccountService} from '../../core/auth/account.service';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {OpenLayersService} from '../../core/map/openlayers.service';
import {NewsFactDetailModalContentComponent} from './news-fact-detail-modal/news-fact-detail-modal.content.component';
import {NewsCategorySelectionService} from '../../core/newscategory/news-category-selection.service';
import {ModalService} from '../../core/modal/modal.service';
import {ROLE_ADMIN, ROLE_CONTRIBUTOR} from '../../shared/constants/role.constants';
import {EventManager} from '../../core/events/event-manager';
import {MapStyleService} from "../../core/map/map-style.service";
import {NewsFactMarkerService} from "../../core/map/news-fact-marker.service";
import {NewsFactMarker} from "../../core/map/news-fact-marker";

@Component({
    selector: 'skis-worldmap',
    templateUrl: './worldmap.component.html',
    styleUrls: ['./worldmap.scss'],
    encapsulation: ViewEncapsulation.None
})
export class WorldmapComponent implements OnInit, AfterViewInit, OnDestroy {

    MAP_ID = 'worldmapPageNewsFactsMap';
    NEWS_FACT_MARKER_ICON_CLICK_TOLERANCE_IN_PIXEL = 3;

    private newsFactsMap: Map;
    private newsFactMarkerLayer: VectorLayer;

    newsFacts: NewsFactNoDetail[];

    categorySelectionSubscription: Subscription;

    constructor(
        private accountService: AccountService,
        private newsFactService: NewsFactService,
        private openLayersService: OpenLayersService,
        private modalService: ModalService,
        private newsCategorySelectionService: NewsCategorySelectionService,
        private eventManager: EventManager,
        private mapboxStyleService: MapStyleService,
        private newsFactMarkerService: NewsFactMarkerService
    ) {
        this.newsFactsMap = null;
        this.newsFactMarkerLayer = null;
    }

    ngOnInit() {
        this.subscribeToNewsCategorySelectionEvents();
    }

    ngAfterViewInit() {
        this.newsFactService.getAll().subscribe(newsFactNoDetails => {
            this.newsFacts = newsFactNoDetails;
            this.buildNewsFactsMap(this.newsFacts);
        });
    }

    ngOnDestroy() {
        if (this.newsFactsMap) {
            this.newsFactsMap.setTarget(null);
        }
        if (this.categorySelectionSubscription) {
            this.eventManager.destroy(this.categorySelectionSubscription);
        }
    }

    subscribeToNewsCategorySelectionEvents() {
        this.categorySelectionSubscription = this.eventManager.subscribe('newsCategorySelectionChanged', (event: { name: string, content: { categoryId: string, isSelected: boolean } }) => {
            this.newsCategorySelectionService.setNewsCategorySelection(event.content.categoryId, event.content.isSelected);
            const selectedCategoryIds = this.newsCategorySelectionService.getSelectedNewsCategoryIds();
            const toShowNewsFacts = this.newsFactService.filterByCategoryIds(this.newsFacts, selectedCategoryIds);
            this.openLayersService.refreshNewsFactMarkerLayer(toShowNewsFacts, this.newsFactMarkerLayer);
        });
    }

    private buildNewsFactsMap(newsFacts: NewsFactNoDetail[]) {
        this.newsFactMarkerLayer = this.openLayersService.buildNewsFactMarkerLayer(newsFacts);
        this.mapboxStyleService.applyAppMapboxStyle(this.MAP_ID).subscribe((map) => {
            this.newsFactsMap = map;
            this.newsFactsMap.addLayer(this.newsFactMarkerLayer);
            this.newsFactsMap.setView(this.openLayersService.buildView([270000, 6250000], 1));
            this.subscribeToNewsFactMarkerClick();
        });
    }

    private showNewsFactDetail(newsFactId: string) {
        this.newsFactService.getNewsFactDetail(newsFactId).subscribe(newsFactDetail => {
            const modalRef = this.modalService.open(NewsFactDetailModalContentComponent, 'news-fact-detail-modal');
            const detailComponentInstance = modalRef.componentInstance as NewsFactDetailModalContentComponent;
            detailComponentInstance.setNewsFactDetail(newsFactDetail);
        });
    }

    private subscribeToNewsFactMarkerClick() {
        this.newsFactsMap.on('click', evt => {
                this.newsFactsMap.forEachFeatureAtPixel(
                    evt.pixel,
                    (clickedClusterFeature: Feature) => {
                        this.newsFactMarkerService.selectMarker(clickedClusterFeature);
                        const newsFactMarkers = clickedClusterFeature.get('features') as NewsFactMarker[];
                        if (newsFactMarkers.length === 1) { // Single news fact (other case can be multiple when news fact are close)
                            this.showNewsFactDetail(newsFactMarkers[0].getNewsFactId());
                        }
                        return true; // Returns true to stop clickedClusterFeature iteration if there was several on the same pixel
                    },
                    {
                        layerFilter: candidate => candidate === this.newsFactMarkerLayer,
                        hitTolerance: this.NEWS_FACT_MARKER_ICON_CLICK_TOLERANCE_IN_PIXEL
                    }
                );
            }
        );
    }

    isAdmin() {
        return this.accountService.hasAnyAuthority(ROLE_ADMIN);
    }

    isContributor() {
        return this.accountService.hasAnyAuthority(ROLE_CONTRIBUTOR);
    }
}
