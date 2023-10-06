package com.vdehorta.skispasse.service;

import com.vdehorta.skispasse.model.dto.AidSummaryDto;
import com.vdehorta.skispasse.model.dto.FindAidsResultDto;
import com.vdehorta.skispasse.model.dto.PersonalDetailDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FindAidsService {

    public FindAidsService() {
    }

    public FindAidsResultDto findAids(PersonalDetailDto personalDetail) {
        List<AidSummaryDto> housingAids = List.of(new AidSummaryDto("APL", "https://www.service-public.fr/particuliers/vosdroits/R1292"));
        return new FindAidsResultDto(Map.of("Logement", housingAids));
    }
}
