package vdehorta.service.errors;

public class MediaAccessException extends RuntimeException {

    public MediaAccessException(String id, Throwable e) {
        super("Error while trying to access to news fact media with id '" + id + "' !", e);
    }
    

}
