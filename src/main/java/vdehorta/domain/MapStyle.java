package vdehorta.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "skis_map_style")
public class MapStyle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("json")
    private String json;


    public MapStyle() {}

    private MapStyle(Builder builder) {
        setId(builder.id);
        setJson(builder.json);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapStyle mapStyle = (MapStyle) o;
        return Objects.equals(id, mapStyle.id) &&
                Objects.equals(json, mapStyle.json);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, json);
    }


    public static final class Builder {
        private String id;
        private String json;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder json(String val) {
            json = val;
            return this;
        }

        public MapStyle build() {
            return new MapStyle(this);
        }
    }
}
