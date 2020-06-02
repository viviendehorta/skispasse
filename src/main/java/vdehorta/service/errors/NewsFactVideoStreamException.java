package vdehorta.service.errors;

public class NewsFactVideoStreamException extends RuntimeException {

    public NewsFactVideoStreamException(String id, Throwable e) {
        super("Cannot stream news fact video with id '" + id + "' !", e);
    }
}
