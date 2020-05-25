package vdehorta.security;

/**
 * Constants for Spring Security authorities.
 */
public enum RoleEnum {

    ADMIN("ROLE_ADMIN"),
    CONTRIBUTOR("ROLE_CONTRIBUTOR"),
    USER("ROLE_USER");

    private String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
