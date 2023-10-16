package com.vdehorta.skispasse.util;

public final class MediaUtil {

    private static final String BASE_MEDIA_URI = "/public/media";

    private MediaUtil() {
    }

    public static String getMediaUrl(String mediaId) {
        return BASE_MEDIA_URI + "/" + mediaId;
    }
}
