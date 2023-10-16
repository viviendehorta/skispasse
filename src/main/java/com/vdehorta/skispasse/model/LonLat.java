package com.vdehorta.skispasse.model;

import java.io.Serializable;

public record LonLat(
        double longitude,
        double latitude) implements Serializable {
}
