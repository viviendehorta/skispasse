package com.vdehorta.skispasse.repository;


import com.vdehorta.skispasse.model.entity.NewsFactDocument;
import com.vdehorta.skispasse.repository.exception.UnexistingNewsFactWithIdException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for MissionDocument
 */
@Repository
public interface NewsFactRepository extends MongoRepository<NewsFactDocument, String> {

    default NewsFactDocument findByIdOrThrowError(String id) throws UnexistingNewsFactWithIdException {
        return this.findById(id).orElseThrow(() -> new UnexistingNewsFactWithIdException(id));
    }
}
