package vdehorta.service.errors;

public class DomainEntityNotFoundException extends RuntimeException {

    private String id;

    public DomainEntityNotFoundException(String capitalizedEntityName, String id) {
        super(capitalizedEntityName + " with id '" + id + "' was not found!");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
