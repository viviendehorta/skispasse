import {Feature} from "ol";
import Point from "ol/geom/Point";
import {NewsFactNoDetail} from "../../shared/model/news-fact-no-detail.model";
import {Coordinate} from "ol/coordinate";

export class NewsFactMarker extends Feature<Point> {

    private readonly newsFactNoDetail: NewsFactNoDetail;

    constructor(newsFactNoDetail:NewsFactNoDetail, coordinate: Coordinate) {
        super({
            geometry: new Point(coordinate),
            newsFactId: newsFactNoDetail.id,
            newsCategoryId: newsFactNoDetail.newsCategoryId
        });
        this.newsFactNoDetail = newsFactNoDetail;
    }

    getNewsFactNoDetail() {
        return this.newsFactNoDetail;
    }
}
