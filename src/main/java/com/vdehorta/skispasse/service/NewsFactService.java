package com.vdehorta.skispasse.service;

import com.vdehorta.skispasse.model.dto.NewsFactDto;
import com.vdehorta.skispasse.model.mapper.NewsFactMapper;
import com.vdehorta.skispasse.repository.NewsFactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsFactService {

    private final NewsFactRepository newsFactRepository;
    private final NewsFactMapper newsFactMapper;

    public NewsFactService(
            NewsFactRepository newsFactRepository,
            NewsFactMapper newsFactMapper) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
    }

    public List<NewsFactDto> list() {
        return newsFactMapper.newsFactDocumentsToNewsFactDtos(newsFactRepository.findAll());
    }
}
