import {Icon, Style} from "ol/style";

const getStyleWithSrc = (src: string) => {
    return new Style({
        image: new Icon({
            src,
            anchor: [0.5, 1],
            opacity: 1
        })
    });
};

/**
 * Size of the marker icons in pixels (icons have to be square, so width or height).
 */
export const MARKER_ICON_SIZE_IN_PIXEL = 32;

/**
 * Style used for displaying multiple news facts that are too close on the map, at the current level of zoom, and that can't be displayed separately
 */
export const MULTIPLE_NEWS_FACTS_STYLE = getStyleWithSrc(`/assets/images/markers/32/double-grey-32.png`);

const STYLE_BY_NEWS_CATEGORY = {
    '1': getStyleWithSrc(`/assets/images/markers/32/red-${MARKER_ICON_SIZE_IN_PIXEL}.png`), // Manifestation
    '2': getStyleWithSrc(`/assets/images/markers/32/blue-${MARKER_ICON_SIZE_IN_PIXEL}.png`), //Sport
    '3': getStyleWithSrc(`/assets/images/markers/32/pink-${MARKER_ICON_SIZE_IN_PIXEL}.png`), //Culture
    '4': getStyleWithSrc(`/assets/images/markers/32/yellow-${MARKER_ICON_SIZE_IN_PIXEL}.png`), //Spectacle
    '5': getStyleWithSrc(`/assets/images/markers/32/green-${MARKER_ICON_SIZE_IN_PIXEL}.png`), //Nature
    '6': getStyleWithSrc(`/assets/images/markers/32/grey-${MARKER_ICON_SIZE_IN_PIXEL}.png`) //Autre
};

/**
 * Return the  style matching the news category with the given id
 * @param categoryId
 */
export const getNewsCategoryStyle = (categoryId: string) => {
    return STYLE_BY_NEWS_CATEGORY[categoryId];
};
