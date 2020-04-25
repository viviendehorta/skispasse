package vdehorta.domain;

import java.util.Objects;

public class LocationCoordinate {

    private long x;
    private long y;

    public LocationCoordinate() {
    }

    public LocationCoordinate(long x, long y) {
        this.x = x;
        this.y = y;
    }

    private LocationCoordinate(Builder builder) {
        setX(builder.x);
        setY(builder.y);
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
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
        private long x;
        private long y;

        public Builder() {
        }

        public Builder x(long val) {
            x = val;
            return this;
        }

        public Builder y(long val) {
            y = val;
            return this;
        }

        public LocationCoordinate build() {
            return new LocationCoordinate(this);
        }
    }
}
