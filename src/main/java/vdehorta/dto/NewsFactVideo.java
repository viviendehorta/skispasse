package vdehorta.dto;

import java.io.InputStream;

public class NewsFactVideo {

    private String newsFactId;
    private InputStream stream; //TODO attention, n'est pas serializable => NewsFactVideo n'est pas un DTO

    public String getNewsFactId() {
        return newsFactId;
    }

    public void setNewsFactId(String newsFactId) {
        this.newsFactId = newsFactId;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }
}
