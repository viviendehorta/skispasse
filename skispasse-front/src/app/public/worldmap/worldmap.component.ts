import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {Vector as VectorLayer} from 'ol/layer';
import {Feature, MapBrowserEvent} from 'ol';
import {Subscription} from 'rxjs';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {AccountService} from '../../core/auth/account.service';
import {NewsFactService} from '../../core/newsfacts/news-fact.service';
import {NewsCategorySelectionService} from '../../core/newscategory/news-category-selection.service';
import {ModalService} from '../../core/modal/modal.service';
import {ROLE_ADMIN, ROLE_CONTRIBUTOR} from '../../shared/constants/role.constants';
import {EventManager} from '../../core/events/event-manager';
import {MapStyleService} from "../../core/map/map-style.service";
import {NewsFactMarkerService} from "../../core/map/news-fact-marker.service";
import {NewsFactMarker} from "../../core/map/news-fact-marker";
import {NewsFactMarkerSelectionService} from "../../core/map/news-fact-marker-selection.service";
import View from "ol/View";
import {Cluster, Vector as VectorSource} from "ol/source";
import {
    MEDIUM_IMG_STYLE_BY_NEWS_CATEGORY,
    MEDIUM_MULTIPLE_NEWS_FACTS_STYLE,
    SMALL_IMG_STYLE_BY_NEWS_CATEGORY,
    SMALL_MARKER_ICON_SIZE_IN_PIXEL,
    SMALL_MULTIPLE_NEWS_FACTS_STYLE
} from "../../core/map/marker-style.constants";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'skis-worldmap',
    templateUrl: './worldmap.component.html'
})
export class WorldmapComponent implements OnInit, AfterViewInit, OnDestroy {

    MAP_ID = 'worldmapPageNewsFactsMap';
    NEWS_FACT_MARKER_ICON_CLICK_TOLERANCE_IN_PIXEL = 3;

    private newsFactsMap: Map;
    private newsFactMarkerLayer: VectorLayer;

    private categorySelectionSubscription: Subscription;
    isNewsFactCreationModeOn: boolean;
    private newsFactCreationModeSubscription: Subscription;

    newsFacts: NewsFactNoDetail[];

    constructor(
        private accountService: AccountService,
        private newsFactService: NewsFactService,
        private modalService: ModalService,
        private newsCategorySelectionService: NewsCategorySelectionService,
        private eventManager: EventManager,
        private mapboxStyleService: MapStyleService,
        private newsFactMarkerService: NewsFactMarkerService,
        private newsFactMarkerSelectionService: NewsFactMarkerSelectionService,
        private route: ActivatedRoute,
        private router: Router,
    ) {
        this.newsFactsMap = null;
        this.newsFactMarkerLayer = null;
    }

    ngOnInit() {
        this.isNewsFactCreationModeOn = false;
        this.subscribeToNewsCategorySelectionEvents();
        this.subscribeToNewsFactCreationModeEvents();
    }

    ngAfterViewInit() {
        this.newsFactService.getAll().subscribe(newsFactNoDetails => {
            this.newsFacts = newsFactNoDetails;
            this.buildMap(this.newsFacts);
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

    isAdmin() {
        return this.accountService.hasAnyAuthority(ROLE_ADMIN);
    }

    isContributor() {
        return this.accountService.hasAnyAuthority(ROLE_CONTRIBUTOR);
    }

    private subscribeToNewsCategorySelectionEvents() {
        this.categorySelectionSubscription = this.eventManager.subscribe('newsCategorySelectionChanged', (event: { name: string, content: { categoryId: string, isSelected: boolean } }) => {
            this.newsCategorySelectionService.setNewsCategorySelection(event.content.categoryId, event.content.isSelected);
            const selectedCategoryIds = this.newsCategorySelectionService.getSelectedNewsCategoryIds();
            const toShowNewsFacts = this.newsFactService.filterByCategoryIds(this.newsFacts, selectedCategoryIds);
            this.refreshNewsFactMarkerLayer(toShowNewsFacts, this.newsFactMarkerLayer);
        });
    }

    private buildMap(newsFacts: NewsFactNoDetail[]) {
        this.newsFactMarkerLayer = this.buildNewsFactMarkerLayer(newsFacts);
        this.mapboxStyleService.applyAppMapboxStyle(this.MAP_ID).subscribe((map) => {
            this.newsFactsMap = map;
            this.newsFactsMap.addLayer(this.newsFactMarkerLayer);
            this.newsFactsMap.setView(this.buildView([270000, 6250000], 1));
            this.subscribeToMapClickEvents();
        });
    }

    private subscribeToMapClickEvents() {
        this.newsFactsMap.on('click', (evt: MapBrowserEvent) => {

            //Uncomment to display coordinates of clicked pixel
            // console.log('Coordinates: ' + JSON.stringify(evt.coordinate));

            if (this.isNewsFactCreationModeOn) { //Contributor user wants to create a news fact occurring here

                this.router.navigate([`/contrib/my-news-facts/new/${evt.coordinate[0]},${evt.coordinate[1]}`]);
            } else { //Check if news fact marker was clicked

                this.newsFactsMap.forEachFeatureAtPixel(
                    evt.pixel,
                    (clickedClusterFeature: Feature) => {
                        this.newsFactMarkerSelectionService.selectMarker(clickedClusterFeature);
                        const newsFactMarkers = clickedClusterFeature.get('features') as NewsFactMarker[];
                        if (newsFactMarkers.length === 1) { // Single news fact

                            //To log clicked news fact coordinates and Latitude/longitude, uncomment below
                            // const feature = newsFactMarkers[0];
                            // const olCoordinates = feature.getGeometry().getCoordinates();
                            // const lonLat = toLonLat(olCoordinates);
                            // console.log(`Coordonnées format ol : ${olCoordinates[0]},${olCoordinates[1]}`);
                            // console.log(`Coordonnées format lonLat : ${lonLat[1]}, ${lonLat[0]}`);

                            this.router.navigate(WorldmapComponent.getNewsFactDetailUrl(newsFactMarkers[0])); //TODO create OutletUrlBuilderService
                        } else if (newsFactMarkers.length > 1) { // News fact group
                            this.router.navigate(WorldmapComponent.getNewsFactGroupUrl(newsFactMarkers)); //TODO create OutletUrlBuilderService
                        }
                        return true; // Returns true to stop clickedClusterFeature iteration if there was several on the same pixel
                    },
                    {
                        layerFilter: candidate => candidate === this.newsFactMarkerLayer,
                        hitTolerance: this.NEWS_FACT_MARKER_ICON_CLICK_TOLERANCE_IN_PIXEL
                    }
                );
            }
        });
    }

    private static getNewsFactDetailUrl(newsFactMarker: NewsFactMarker): any[] {
        return [
            '',
            {
                outlets: {
                    modal: [
                        'newsFact',
                        newsFactMarker.getNewsFactNoDetail().id
                    ]
                }
            }
        ];
    }

    private static getNewsFactGroupUrl(newsFactMarkers: NewsFactMarker[]): any[] {
        return [
            '',
            {
                outlets: {
                    modal: [
                        "newsFactGroup",
                        newsFactMarkers.map(marker => marker.getNewsFactNoDetail().id).join(',')
                    ]
                }
            }
        ];
    }

    private buildView(center: number[], zoom: number): View {
        return new View({
            center,
            zoom,
            constrainResolution: true
        });
    }

    private buildNewsFactMarkerLayer(newsFacts: NewsFactNoDetail[]): VectorLayer {
        const newsFactMarkers = this.newsFactMarkerService.toNewsFactMarkers(newsFacts);

        const clusterSource = new Cluster({
            distance: SMALL_MARKER_ICON_SIZE_IN_PIXEL + 4, // Add 4 pixels to MARKER_ICON_SIZE_IN_PIXEL to never have icon collision
            source: new VectorSource({features: newsFactMarkers})
        });

        return new VectorLayer({
            source: clusterSource,
            style: (feature) => {

                const clusterFeatures = feature.get('features') as Feature[];
                if (clusterFeatures.length > 1) { // Several news facts in the cluster, use group icon
                    if (this.newsFactMarkerSelectionService.isSelectedMarker(feature)) {
                        return MEDIUM_MULTIPLE_NEWS_FACTS_STYLE;
                    }
                    return SMALL_MULTIPLE_NEWS_FACTS_STYLE;
                }
                if (this.newsFactMarkerSelectionService.isSelectedMarker(feature)) {
                    return MEDIUM_IMG_STYLE_BY_NEWS_CATEGORY[clusterFeatures[0].get('newsCategoryId')];
                }
                return SMALL_IMG_STYLE_BY_NEWS_CATEGORY[clusterFeatures[0].get('newsCategoryId')];
            }
        });
    }

    private refreshNewsFactMarkerLayer(newsFacts: NewsFactNoDetail[], newsFactMarkerLayer: VectorLayer): void {
        const cluster = newsFactMarkerLayer.getSource() as Cluster;
        cluster.getSource().clear();
        cluster.getSource().addFeatures(this.newsFactMarkerService.toNewsFactMarkers(newsFacts));
    }

    private subscribeToNewsFactCreationModeEvents() {
        this.newsFactCreationModeSubscription = this.eventManager.subscribe('switchNewsFactCreationMode', () => {
            this.isNewsFactCreationModeOn = !this.isNewsFactCreationModeOn;
        });
    }
}
