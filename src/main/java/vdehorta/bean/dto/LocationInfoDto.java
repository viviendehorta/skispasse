package vdehorta.bean.dto;

import java.util.Objects;

public class LocationInfoDto {

    private String country;

    private String city;

    private String locality;

    public LocationInfoDto() {}

    private LocationInfoDto(Builder builder) {
        country = builder.country;
        city = builder.city;
        locality = builder.locality;
    }


    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getLocality() {
        return locality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationInfoDto that = (LocationInfoDto) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(locality, that.locality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, locality);
    }

    public static final class Builder {
        private String country;
        private String city;
        private String locality;

        public Builder() {
        }

        public Builder country(String val) {
            country = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public Builder locality(String val) {
            locality = val;
            return this;
        }

        public LocationInfoDto build() {
            return new LocationInfoDto(this);
        }
    }
}
