package com.vdehorta.skispasse.repository.exception;

public class UnexistingEntityWithIdException extends RuntimeException {

    private final String entityType;
    private final String entityId;

    public UnexistingEntityWithIdException(String entityType, String entityId) {
        super("Unexisting " + entityType + " with id " + entityId + ".");
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }
}
