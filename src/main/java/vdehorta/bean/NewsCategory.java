package vdehorta.bean;

import java.util.Arrays;

public enum NewsCategory {

    DEMONSTRATION(1),
    SPORT(2),
    CULTURE(3),
    SHOW(4),
    NATURE(5),
    OTHER(6);

    private int id;

    NewsCategory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static NewsCategory fromId(int id) {
        NewsCategory[] newsCategories = values();
        return Arrays.stream(newsCategories)
            .filter(newsCategory -> newsCategory.getId() == id)
            .findFirst()
            .orElse(null);
    }
}
