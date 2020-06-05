package vdehorta.bean.dto;

import java.util.Objects;

public class NewsCategoryDto {

    private String id;

    private String label;

    public NewsCategoryDto() {
    }

    private NewsCategoryDto(Builder builder) {
        id = builder.id;
        label = builder.label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        private String id;
        private String label;

        public Builder() {
        }

        public Builder id(String val) {
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
