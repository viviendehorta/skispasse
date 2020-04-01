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

  private newsCategories: any[];

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
    this.newsCategories = this.newsCategoryService.getCategories();
  }

  ngAfterViewInit() {
    this.newsFactService.fetchNewsFacts().subscribe(unflattenedNewsFacts => {
      this.newsFactService.flattenNewsFacts(unflattenedNewsFacts);
      this.buildNewsFactsMap(this.newsFactService.getAll());
    });
  }

  buildNewsFactsMap(newsFacts: any[]) {
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

  onCategoryChanged(categoryChangedEvent: { categoryValue: string; isSelected: boolean }): void {
    this.newsCategoryService.setCategorySelection(categoryChangedEvent.categoryValue, categoryChangedEvent.isSelected);
    const selectedCategoryIds = this.newsCategoryService.getSelectedCategoryIds();
    const toShowNewsFacts = this.newsFactService.getFilteredByCategoryIds(selectedCategoryIds);
    this.openLayersService.refreshLayerFeatures(this.mappingService.newsFactNoDetailsToFeatures(toShowNewsFacts), this.newsFactMarkerLayer);
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
