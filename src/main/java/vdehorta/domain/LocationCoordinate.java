package vdehorta.domain;

import java.util.Objects;

public class LocationCoordinate {

    private Long x;
    private Long y;

    public LocationCoordinate() {
    }

    public LocationCoordinate(Long x, Long y) {
        this.x = x;
        this.y = y;
    }

    private LocationCoordinate(Builder builder) {
        setX(builder.x);
        setY(builder.y);
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationCoordinate that = (LocationCoordinate) o;
        return x == that.x &&
            y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


    public static final class Builder {
        private Long x;
        private Long y;

        public Builder() {
        }

        public Builder x(Long val) {
            x = val;
            return this;
        }

        public Builder y(Long val) {
            y = val;
            return this;
        }

        public LocationCoordinate build() {
            return new LocationCoordinate(this);
        }
    }
}
