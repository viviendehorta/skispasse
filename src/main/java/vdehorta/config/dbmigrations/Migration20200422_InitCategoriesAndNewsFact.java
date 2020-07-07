package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.service.ClockService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ChangeLog(order = "20200422")
public class Migration20200422_InitCategoriesAndNewsFact {

    private ClockService clockService = new ClockService();

    @ChangeSet(order = "01", author = "admin", id = "01-addInitialCategories")
    public void addInitialCategories(MongoTemplate mongoTemplate) {

        final NewsCategory.Builder newsCategoryBuilder = new NewsCategory.Builder();
        final List<NewsCategory> initialNewsCategories = Arrays.asList(
                newsCategoryBuilder.id("1").label("Manifestation").build(),
                newsCategoryBuilder.id("2").label("Sport").build(),
                newsCategoryBuilder.id("3").label("Culture").build(),
                newsCategoryBuilder.id("4").label("Spectacle").build(),
                newsCategoryBuilder.id("5").label("Nature").build(),
                newsCategoryBuilder.id("6").label("Autre").build()
        );

        mongoTemplate.insertAll(initialNewsCategories);
    }

    @ChangeSet(order = "02", author = "admin", id = "02-addInitialNewsFact")
    public void addInitialNewsFact(MongoTemplate mongoTemplate) {
        List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);

        assert allCategories.size() == 6;

        LocalDateTime now = LocalDateTime.now();
        NewsCategory newsCategory = allCategories.get(0);
        NewsFact initialNewsFact = new NewsFact.Builder()
                .newsCategoryId(newsCategory.getId())
                .newsCategoryLabel(newsCategory.getLabel())
                .address("12 Place de la République, 75011 Bondy, France")
                .city("Vila Real de Santo Antonio")
                .country("Portugal")
                .owner("contributor")
                .eventDate(clockService.now())
                .locationCoordinateX(-825497.1763430884)
                .locationCoordinateY(4466253.087107279)
                .createdDate(now)
                .lastModifiedDate(now)
                .build();

        mongoTemplate.insert(initialNewsFact);
    }
}
