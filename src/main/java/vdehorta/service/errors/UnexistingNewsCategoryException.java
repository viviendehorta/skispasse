package vdehorta.service.errors;

public class UnexistingNewsCategoryException extends RuntimeException {

    private String id;

    public UnexistingNewsCategoryException(String id) {
        super("Unexisting news category id '" + id + "' !");
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
