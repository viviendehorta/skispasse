package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Migration: 21/04/2020
 */
@ChangeLog(order = "20200409")
public class Migration20200422 {

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

    @ChangeSet(order = "02", author = "admin", id = "02-addInitialNewsFacts")
    public void addInitialNewsFacts(MongoTemplate mongoTemplate) {

        final long baseCoord = 4500000L;

        final List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);

        assert allCategories.size() == 6;

        final NewsFact.Builder newsFactBuilder = new NewsFact.Builder();
        final List<NewsFact> initialNewsFacts = Arrays.asList(
            newsFactBuilder.categoryId(allCategories.get(0).getId()).address("12 Place de la République, 75011 Bondy, France").city("Bondy").country("Portugal").owner("contributor").eventDate(Instant.now()).videoPath("/content/videos/small.mp4").geoCoordinateX(baseCoord).geoCoordinateY(baseCoord).build(),
            newsFactBuilder.categoryId(allCategories.get(1).getId()).address("5 Place de la République, 75011 Montreuil, France").city("Montreuil").country("France").owner("contributor").eventDate(Instant.now()).videoPath("/content/videos/small.mp4").geoCoordinateX(baseCoord / 2).geoCoordinateY(baseCoord).build(),
            newsFactBuilder.categoryId(allCategories.get(2).getId()).address("8 Place de la République, 75011 Paris, France").city("Paris").country("Brésil").owner("contributor").eventDate(Instant.now()).videoPath("/content/videos/small.mp4").geoCoordinateX(baseCoord).geoCoordinateY(baseCoord / 3).build(),
            newsFactBuilder.categoryId(allCategories.get(3).getId()).address("76 Place de la République, 75011 Aulnay, France").city("Aulnay").country("Chili").owner("contributor").eventDate(Instant.now()).videoPath("/content/videos/small.mp4").geoCoordinateX(baseCoord / 4).geoCoordinateY(baseCoord / 4).build(),
            newsFactBuilder.categoryId(allCategories.get(4).getId()).address("Place de la République, 75011 Bobigny, France").city("Bobigny").country("Argentine").owner("contributor").eventDate(Instant.now()).videoPath("/content/videos/small.mp4").geoCoordinateX(baseCoord * 2).geoCoordinateY(baseCoord / 5).build(),
            newsFactBuilder.categoryId(allCategories.get(5).getId()).address("9 Place de la République, 75011 Grenoble, France").city("Grenoble").country("Colombie").owner("contributor").eventDate(Instant.now()).videoPath("/content/videos/small.mp4").geoCoordinateX(baseCoord).geoCoordinateY(baseCoord * 2).build()
        );

        mongoTemplate.insertAll(initialNewsFacts);
    }
}