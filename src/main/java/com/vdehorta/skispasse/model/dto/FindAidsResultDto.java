package com.vdehorta.skispasse.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record FindAidsResultDto(Map<String, List<AidSummaryDto>> aidsByCategory) implements Serializable {}
