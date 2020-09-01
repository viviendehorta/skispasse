package vdehorta.service.errors;

public class MediaNotFoundException extends DomainEntityNotFoundException {

    public MediaNotFoundException(String mediaId) {
        super("News fact media", mediaId);
    }
}
