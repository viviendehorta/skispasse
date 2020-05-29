package vdehorta.dto;

public class AuthenticatedDto {

    private boolean authenticated;
    private UserDto user;

    private AuthenticatedDto(Builder builder) {
        authenticated = builder.authenticated;
        user = builder.user;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public UserDto getUser() {
        return user;
    }


    public static final class Builder {
        private boolean authenticated;
        private UserDto user;

        public Builder() {
        }

        public Builder isAuthenticated(boolean val) {
            authenticated = val;
            return this;
        }

        public Builder user(UserDto val) {
            user = val;
            return this;
        }

        public AuthenticatedDto build() {
            return new AuthenticatedDto(this);
        }
    }
}
