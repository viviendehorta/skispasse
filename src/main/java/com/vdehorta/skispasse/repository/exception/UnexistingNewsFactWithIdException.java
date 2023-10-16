package com.vdehorta.skispasse.repository.exception;

public class UnexistingNewsFactWithIdException extends UnexistingEntityWithIdException {

    public UnexistingNewsFactWithIdException(String id) {
        super("newsfact", id);
    }
}
