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

/**
 * Icon files by news category id
 */
export const PNG_BY_NEWS_CATEGORY = {
    '1': `/assets/images/markers/32/red-${MARKER_ICON_SIZE_IN_PIXEL}.png`, // Manifestation
    '2': `/assets/images/markers/32/blue-${MARKER_ICON_SIZE_IN_PIXEL}.png`, //Sport
    '3': `/assets/images/markers/32/pink-${MARKER_ICON_SIZE_IN_PIXEL}.png`, //Culture
    '4': `/assets/images/markers/32/yellow-${MARKER_ICON_SIZE_IN_PIXEL}.png`, //Spectacle
    '5': `/assets/images/markers/32/green-${MARKER_ICON_SIZE_IN_PIXEL}.png`, //Nature
    '6': `/assets/images/markers/32/grey-${MARKER_ICON_SIZE_IN_PIXEL}.png` //Autre
};

/**
 * Openlayers styles by news category id
 */
export const STYLE_BY_NEWS_CATEGORY = {
    '1': getStyleWithSrc(PNG_BY_NEWS_CATEGORY[1]), // Manifestation
    '2': getStyleWithSrc(PNG_BY_NEWS_CATEGORY[2]), //Sport
    '3': getStyleWithSrc(PNG_BY_NEWS_CATEGORY[3]), //Culture
    '4': getStyleWithSrc(PNG_BY_NEWS_CATEGORY[4]), //Spectacle
    '5': getStyleWithSrc(PNG_BY_NEWS_CATEGORY[5]), //Nature
    '6': getStyleWithSrc(PNG_BY_NEWS_CATEGORY[6]) //Autre
};
