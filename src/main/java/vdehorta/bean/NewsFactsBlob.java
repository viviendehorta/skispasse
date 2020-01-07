package vdehorta.bean;

import java.util.Collection;

public class NewsFactsBlob {

    private Collection<NewsFact> newsFacts;

    public NewsFactsBlob(Collection<NewsFact> newsFacts) {
        this.newsFacts = newsFacts;
    }

    public Collection<NewsFact> getNewsFacts() {
        return newsFacts;
    }

    public void setNewsFacts(Collection<NewsFact> newsFacts) {
        this.newsFacts = newsFacts;
    }
}
