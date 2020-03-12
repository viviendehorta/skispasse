package vdehorta.bean;

public class NewsCategory {

    private int id;
    private String label;

    private NewsCategory(Builder builder) {
        id = builder.id;
        label = builder.label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
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

        public NewsCategory build() {
            return new NewsCategory(this);
        }
    }
}
