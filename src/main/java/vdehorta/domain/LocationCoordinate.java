package vdehorta.domain;

import java.util.Objects;

public class LocationCoordinate {

    private double x;
    private double y;

    public LocationCoordinate() {
    }

    public LocationCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private LocationCoordinate(Builder builder) {
        setX(builder.x);
        setY(builder.y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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
        private double x;
        private double y;

        public Builder() {
        }

        public Builder x(double val) {
            x = val;
            return this;
        }

        public Builder y(double val) {
            y = val;
            return this;
        }

        public LocationCoordinate build() {
            return new LocationCoordinate(this);
        }
    }
}
