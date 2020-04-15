package vdehorta.repository;

import vdehorta.domain.NewsCategory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NewsCategoryRepository {

    private final NewsCategory.Builder NEWS_CATEGORY_BUILDER = new NewsCategory.Builder();

    protected final List<NewsCategory> NEWS_CATEGORIES = Arrays.asList(
        NEWS_CATEGORY_BUILDER.id(1).label("Manifestation").build(),
        NEWS_CATEGORY_BUILDER.id(2).label("Sport").build(),
        NEWS_CATEGORY_BUILDER.id(3).label("Culture").build(),
        NEWS_CATEGORY_BUILDER.id(4).label("Spectacle").build(),
        NEWS_CATEGORY_BUILDER.id(5).label("Nature").build(),
        NEWS_CATEGORY_BUILDER.id(6).label("Autre").build()
    );

    protected Map<Integer, NewsCategory> NEWS_CATEGORIES_BY_ID = NEWS_CATEGORIES.stream().collect(Collectors.toMap(NewsCategory::getId, Function.identity()));

    public List<NewsCategory> getAll() {
        return new ArrayList<>(NEWS_CATEGORIES);
    }

    public NewsCategory getById(int id) {
        return NEWS_CATEGORIES_BY_ID.get(id);
    }
}
