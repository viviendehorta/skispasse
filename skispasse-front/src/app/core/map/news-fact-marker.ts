import {Feature} from "ol";
import Point from "ol/geom/Point";
import {NewsFactNoDetail} from "../../shared/model/news-fact-no-detail.model";

export class NewsFactMarker extends Feature<Point> {

    private readonly newsFactNoDetail: NewsFactNoDetail;

    constructor(newsFactNoDetail:NewsFactNoDetail) {
        super({
            geometry: new Point([newsFactNoDetail.locationCoordinate.x, newsFactNoDetail.locationCoordinate.y]),
            newsFactId: newsFactNoDetail.id,
            newsCategoryId: newsFactNoDetail.newsCategoryId
        });
        this.newsFactNoDetail = newsFactNoDetail;
    }

    getNewsFactNoDetail() {
        return this.newsFactNoDetail;
    }
}
