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
export const SMALL_MARKER_ICON_SIZE_IN_PIXEL = 32;
export const MEDIUM_MARKER_ICON_SIZE_IN_PIXEL = 40;

/**
 * Style used for displaying multiple news facts that are too close on the map, at the current level of zoom, and that can't be displayed separately
 */
export const SMALL_MULTIPLE_NEWS_FACTS_STYLE = getStyleWithSrc(`/assets/images/markers/32/double-grey-32.png`);
export const MEDIUM_MULTIPLE_NEWS_FACTS_STYLE = getStyleWithSrc(`/assets/images/markers/40/double-grey-40.png`);

export const SMALL_PNG_BY_NEWS_CATEGORY = {
    '1': `/assets/images/markers/${SMALL_MARKER_ICON_SIZE_IN_PIXEL}/red-${SMALL_MARKER_ICON_SIZE_IN_PIXEL}.png`, // Manifestation
    '2': `/assets/images/markers/${SMALL_MARKER_ICON_SIZE_IN_PIXEL}/blue-${SMALL_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Sport
    '3': `/assets/images/markers/${SMALL_MARKER_ICON_SIZE_IN_PIXEL}/pink-${SMALL_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Culture
    '4': `/assets/images/markers/${SMALL_MARKER_ICON_SIZE_IN_PIXEL}/yellow-${SMALL_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Spectacle
    '5': `/assets/images/markers/${SMALL_MARKER_ICON_SIZE_IN_PIXEL}/green-${SMALL_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Nature
    '6': `/assets/images/markers/${SMALL_MARKER_ICON_SIZE_IN_PIXEL}/purple-${SMALL_MARKER_ICON_SIZE_IN_PIXEL}.png` //Autre
};

export const MEDIUM_PNG_BY_NEWS_CATEGORY = {
    '1': `/assets/images/markers/${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}/red-${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}.png`, // Manifestation
    '2': `/assets/images/markers/${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}/blue-${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Sport
    '3': `/assets/images/markers/${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}/pink-${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Culture
    '4': `/assets/images/markers/${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}/yellow-${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Spectacle
    '5': `/assets/images/markers/${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}/green-${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}.png`, //Nature
    '6': `/assets/images/markers/${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}/purple-${MEDIUM_MARKER_ICON_SIZE_IN_PIXEL}.png` //Autre
};

export const SMALL_IMG_STYLE_BY_NEWS_CATEGORY = {
    '1': getStyleWithSrc(SMALL_PNG_BY_NEWS_CATEGORY[1]), // Manifestation
    '2': getStyleWithSrc(SMALL_PNG_BY_NEWS_CATEGORY[2]), //Sport
    '3': getStyleWithSrc(SMALL_PNG_BY_NEWS_CATEGORY[3]), //Culture
    '4': getStyleWithSrc(SMALL_PNG_BY_NEWS_CATEGORY[4]), //Spectacle
    '5': getStyleWithSrc(SMALL_PNG_BY_NEWS_CATEGORY[5]), //Nature
    '6': getStyleWithSrc(SMALL_PNG_BY_NEWS_CATEGORY[6]) //Autre
};

export const MEDIUM_IMG_STYLE_BY_NEWS_CATEGORY = {
    '1': getStyleWithSrc(MEDIUM_PNG_BY_NEWS_CATEGORY[1]), // Manifestation
    '2': getStyleWithSrc(MEDIUM_PNG_BY_NEWS_CATEGORY[2]), //Sport
    '3': getStyleWithSrc(MEDIUM_PNG_BY_NEWS_CATEGORY[3]), //Culture
    '4': getStyleWithSrc(MEDIUM_PNG_BY_NEWS_CATEGORY[4]), //Spectacle
    '5': getStyleWithSrc(MEDIUM_PNG_BY_NEWS_CATEGORY[5]), //Nature
    '6': getStyleWithSrc(MEDIUM_PNG_BY_NEWS_CATEGORY[6]) //Autre
};
