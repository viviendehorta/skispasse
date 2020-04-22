package vdehorta.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "skis_news_category")
public class NewsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("label")
    private String label;

    private NewsCategory() {}

    private NewsCategory(Builder builder) {
        id = builder.id;
        label = builder.label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsCategory that = (NewsCategory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "NewsCategory{" +
            "id=" + id +
            ", label='" + label + '\'' +
            '}';
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

        public NewsCategory build() {
            return new NewsCategory(this);
        }
    }
}
