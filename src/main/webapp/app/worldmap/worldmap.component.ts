import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import Map from 'ol/Map';
import { NewsFactService } from 'app/core/newsfacts/news-facts.service';
import { OpenLayersService } from 'app/core/openlayers/openlayers.service';
import { Vector as VectorLayer } from 'ol/layer';
import { MappingService } from 'app/core/mapping/mapping.service';
import { Feature } from 'ol';
import { NewsFactDetailModalContentComponent } from 'app/worldmap/news-fact-detail-modal/news-fact-detail-modal.content.component';
import { NewsFactNoDetail } from 'app/shared/beans/news-fact-no-detail.model';
import { ModalService } from 'app/core/modal/modal.service';
import { NewsCategorySelectionService } from 'app/worldmap/news-category-selection.service';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { ROLE_ADMIN, ROLE_CONTRIBUTOR } from 'app/shared/constants/role.constants';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'skis-worldmap',
  templateUrl: './worldmap.component.html',
  styleUrls: ['./worldmap.scss']
})
export class WorldmapComponent implements OnInit, AfterViewInit, OnDestroy {
  // eslint-disable-next-line @typescript-eslint/no-this-alias
  private instance = this; // hack to be able to reach worldmapComponent instance in callback without word 'this'

  MAP_ID = 'worldmapPageNewsFactsMap';
  ICON_PIXEL_CLICK_TOLERANCE = 3;

  private newsFactsMap: Map;
  private newsFactMarkerLayer: VectorLayer;

  newsFacts: NewsFactNoDetail[];

  categorySelectionSubscription: Subscription;

  constructor(
    private accountService: AccountService,
    private newsFactService: NewsFactService,
    private openLayersService: OpenLayersService,
    private mappingService: MappingService,
    private modalService: ModalService,
    private newsCategorySelectionService: NewsCategorySelectionService,
    private eventManager: JhiEventManager
  ) {
    this.newsFactsMap = null;
    this.newsFactMarkerLayer = null;
  }

  ngOnInit() {
    this.registerChangeInCategories();
  }

  ngAfterViewInit() {
    this.newsFactService.fetchNewsFacts().subscribe(unflattenedNewsFacts => {
      this.newsFacts = this.newsFactService.flattenNewsFacts(unflattenedNewsFacts);
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

  registerChangeInCategories() {
    this.categorySelectionSubscription = this.eventManager.subscribe('newsCategorySelectionChanged', event =>
      this.onNewsCategorySelectionChanged(event)
    );
  }

  buildNewsFactsMap(newsFacts: NewsFactNoDetail[]) {
    const view = this.openLayersService.buildView([270000, 6250000], 1);
    const oSMLayer = this.openLayersService.buildOSMTileLayer();
    this.newsFactMarkerLayer = this.openLayersService.buildMarkerVectorLayer(newsFacts);

    this.newsFactsMap = new Map({
      layers: [oSMLayer, this.newsFactMarkerLayer],
      target: this.MAP_ID,
      view
    });

    // Behaviour when new fact markers are clicked : displaying detail modal
    this.newsFactsMap.on('click', evt => {
      this.newsFactsMap.forEachFeatureAtPixel(
        evt.pixel,
        (feature: Feature) => {
          function showNewsFactDetail(worldmapInstance: WorldmapComponent, newsFactId: number): void {
            worldmapInstance.showNewsFactDetail(newsFactId);
          }

          showNewsFactDetail(this.instance, feature.get('newsFactId'));
          return true; // Returns true to stop feature iteration if there was several on the same pixel
        },
        {
          layerFilter: layerCandidate => {
            return layerCandidate === this.newsFactMarkerLayer;
          },
          hitTolerance: this.ICON_PIXEL_CLICK_TOLERANCE
        }
      );
    });
  }

  showNewsFactDetail(newsFactId: number) {
    this.newsFactService.getNewsFactDetail(newsFactId).subscribe(unFlattenedNewsFactDetail => {
      const newsFactDetail = this.newsFactService.flattenNewsFactDetail(unFlattenedNewsFactDetail);
      const modalRef = this.modalService.open(NewsFactDetailModalContentComponent, 'news-fact-detail-modal');
      const detailComponentInstance = modalRef.componentInstance as NewsFactDetailModalContentComponent;
      detailComponentInstance.setNewsFactDetail(newsFactDetail);
    });
  }

  onNewsCategorySelectionChanged(event: { name: string; content: { categoryId: number; isSelected: boolean } }): void {
    this.newsCategorySelectionService.setCategorySelection(event.content.categoryId, event.content.isSelected);
    const selectedCategoryIds = this.newsCategorySelectionService.getSelectedNewsCategoryIds();
    const toShowNewsFacts = this.newsFactService.filterByCategoryIds(this.newsFacts, selectedCategoryIds);
    this.openLayersService.refreshLayerFeatures(this.mappingService.newsFactNoDetailsToFeatures(toShowNewsFacts), this.newsFactMarkerLayer);
  }

  isAdmin() {
    return this.accountService.getRole() === ROLE_ADMIN;
  }

  isContributor() {
    return this.accountService.getRole() === ROLE_CONTRIBUTOR;
  }
}
