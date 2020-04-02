import { AfterViewInit, Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { NewsCategoryService } from 'app/core/newscategory/news-category.service';
import Map from 'ol/Map';
import { NewsFactService } from 'app/core/newsfacts/news-facts.service';
import { OpenLayersService } from 'app/core/openlayers/openlayers.service';
import { Vector as VectorLayer } from 'ol/layer';
import { MappingService } from 'app/core/mapping/mapping.service';
import { Feature } from 'ol';
import { NewsFactDetailModalContentComponent } from 'app/map/news-fact-detail-modal/news-fact-detail-modal.content.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NewsCategory } from 'app/shared/beans/news-category.model';
import { CategoryChangedEvent } from 'app/shared/beans/events/category-changed.event.model';
import { NewsFactNoDetail } from 'app/shared/beans/news-fact-no-detail.model';

@Component({
  selector: 'skis-worldmap',
  templateUrl: './worldmap.component.html',
  styleUrls: ['./worldmap.scss'],
  encapsulation: ViewEncapsulation.None
})
export class WorldmapComponent implements OnInit, AfterViewInit, OnDestroy {
  // eslint-disable-next-line @typescript-eslint/no-this-alias
  private instance = this; // hack to be able to reach worldmapComponent instance in callback without word 'this'

  MAP_ID = 'worldmapPageNewsFactsMap';
  ICON_PIXEL_CLICK_TOLERANCE = 3;

  private newsFactsMap: Map;
  private newsFactMarkerLayer: VectorLayer;

  newsFacts: NewsFactNoDetail[];
  newsCategories: NewsCategory[];

  constructor(
    private newsFactService: NewsFactService,
    private openLayersService: OpenLayersService,
    private newsCategoryService: NewsCategoryService,
    private mappingService: MappingService,
    private modalService: NgbModal
  ) {
    this.newsFactsMap = null;
    this.newsFactMarkerLayer = null;
    this.newsCategories = [];
  }

  ngOnInit() {
    this.newsCategoryService.fetchCategories().subscribe(unflattenedNewsCategories => {
      this.newsCategories = this.newsCategoryService.flattenNewsCategories(unflattenedNewsCategories);
    });
  }

  ngAfterViewInit() {
    this.newsFactService.fetchNewsFacts().subscribe(unflattenedNewsFacts => {
      this.newsFacts = this.newsFactService.flattenNewsFacts(unflattenedNewsFacts);
      this.buildNewsFactsMap(this.newsFacts);
    });
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

  onCategoryChanged(categoryChangedEvent: CategoryChangedEvent): void {
    this.setCategorySelection(categoryChangedEvent.categoryId, categoryChangedEvent.isSelected);
    const selectedCategoryIds = this.getSelectedCategoryIds();
    const toShowNewsFacts = this.newsFactService.filterByCategoryIds(this.newsFacts, selectedCategoryIds);
    this.openLayersService.refreshLayerFeatures(this.mappingService.newsFactNoDetailsToFeatures(toShowNewsFacts), this.newsFactMarkerLayer);
  }

  setCategorySelection(categoryId: number, isSelected: boolean) {
    const category = this.newsCategories.find(newsCategory => newsCategory.id === categoryId);
    category.isSelected = isSelected;
  }

  getSelectedCategoryIds(): number[] {
    return this.newsCategories.filter(category => category.isSelected).map(category => category.id);
  }

  showNewsFactDetail(newsFactId: number) {
    this.newsFactService.getNewsFactDetail(newsFactId).subscribe(unFlattenedNewsFactDetail => {
      const newsFactDetail = this.newsFactService.flattenNewsFactDetail(unFlattenedNewsFactDetail);
      const detailComponentInstance = this.modalService.open(NewsFactDetailModalContentComponent, {
        centered: true,
        windowClass: 'news-fact-detail-modal'
      }).componentInstance as NewsFactDetailModalContentComponent;
      detailComponentInstance.setNewsFactDetail(newsFactDetail);
    });
  }

  ngOnDestroy() {
    if (this.newsFactsMap) {
      this.newsFactsMap.setTarget(null);
    }
  }
}
