import {Feature} from "ol";
import Point from "ol/geom/Point";
import {LocationCoordinate} from "../../shared/model/location-coordinate.model";

export class NewsFactMarker extends Feature<Point> {

    private locationCoordinate: LocationCoordinate;
    private newsFactId: string;
    private mewsCategoryId: string;

    constructor(locationCoordinate: LocationCoordinate, newsFactId: string, newsCategoryId: string) {
        super({
            geometry: new Point([locationCoordinate.x, locationCoordinate.y]),
            newsFactId,
            newsCategoryId
        });
        this.locationCoordinate = locationCoordinate;
        this.newsFactId = newsFactId;
        this.mewsCategoryId = newsCategoryId;
    }

    getLocationCoordinate(): LocationCoordinate {
        return this.locationCoordinate;
    }

    getNewsFactId(): string {
        return this.newsFactId;
    }

    getNewsCategoryId(): string {
        return this.mewsCategoryId;
    }
}
