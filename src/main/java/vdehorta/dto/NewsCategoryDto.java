package vdehorta.dto;

import java.util.Objects;

public class NewsCategoryDto {

    private int id;

    private String label;

    private NewsCategoryDto(Builder builder) {
        id = builder.id;
        label = builder.label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsCategoryDto that = (NewsCategoryDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private int id;
        private String label;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder label(String val) {
            label = val;
            return this;
        }

        public NewsCategoryDto build() {
            return new NewsCategoryDto(this);
        }
    }
}
