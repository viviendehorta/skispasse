package vdehorta.bean.dto;

public class AuthenticationDto {

    private boolean authenticated;
    private UserDto user;

    private AuthenticationDto(Builder builder) {
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

        public AuthenticationDto build() {
            return new AuthenticationDto(this);
        }
    }
}
