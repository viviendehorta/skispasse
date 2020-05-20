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

/**
 * Migration: 21/04/2020
 */
@ChangeLog(order = "20200409")
public class Migration20200422 {

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

    @ChangeSet(order = "02", author = "admin", id = "02-addInitialNewsFacts")
    public void addInitialNewsFacts(MongoTemplate mongoTemplate) {

        final Long baseCoord = 4500000L;

        final List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);

        assert allCategories.size() == 6;

        final NewsFact.Builder newsFactBuilder = new NewsFact.Builder();
        final LocalDateTime now = LocalDateTime.now();

        final List<NewsFact> initialNewsFacts = Arrays.asList(
                newsFactBuilder.newsCategoryId(allCategories.get(0).getId()).newsCategoryLabel(allCategories.get(0).getLabel()).address("12 Place de la République, 75011 Bondy, France").city("Bondy").country("Portugal").owner("contributor").eventDate(clockService.now()).videoPath("/assets/videos/small.mp4").locationCoordinateX(baseCoord).locationCoordinateY(baseCoord).createdDate(now).lastModifiedDate(now).build(),
                newsFactBuilder.newsCategoryId(allCategories.get(1).getId()).newsCategoryLabel(allCategories.get(1).getLabel()).address("5 Place de la République, 75011 Montreuil, France").city("Montreuil").country("France").owner("contributor").eventDate(clockService.now()).videoPath("/assets/videos/small.mp4").locationCoordinateX(baseCoord / 2).locationCoordinateY(baseCoord).createdDate(now).lastModifiedDate(now).build(),
                newsFactBuilder.newsCategoryId(allCategories.get(2).getId()).newsCategoryLabel(allCategories.get(2).getLabel()).address("8 Place de la République, 75011 Paris, France").city("Paris").country("Brésil").owner("contributor").eventDate(clockService.now()).videoPath("/assets/videos/small.mp4").locationCoordinateX(baseCoord).locationCoordinateY(baseCoord / 3).createdDate(now).lastModifiedDate(now).build(),
        newsFactBuilder.newsCategoryId(allCategories.get(3).getId()).newsCategoryLabel(allCategories.get(3).getLabel()).address("76 Place de la République, 75011 Aulnay, France").city("Aulnay").country("Chili").owner("contributor").eventDate(clockService.now()).videoPath("/assets/videos/small.mp4").locationCoordinateX(baseCoord / 4).locationCoordinateY(baseCoord / 4).createdDate(now).lastModifiedDate(now).build(),
                newsFactBuilder.newsCategoryId(allCategories.get(4).getId()).newsCategoryLabel(allCategories.get(4).getLabel()).address("Place de la République, 75011 Bobigny, France").city("Bobigny").country("Argentine").owner("contributor").eventDate(clockService.now()).videoPath("/assets/videos/small.mp4").locationCoordinateX(baseCoord * 2).locationCoordinateY(baseCoord / 5).createdDate(now).lastModifiedDate(now).build(),
                newsFactBuilder.newsCategoryId(allCategories.get(5).getId()).newsCategoryLabel(allCategories.get(5).getLabel()).address("9 Place de la République, 75011 Grenoble, France").city("Grenoble").country("Colombie").owner("contributor").eventDate(clockService.now()).videoPath("/assets/videos/small.mp4").locationCoordinateX(baseCoord).locationCoordinateY(baseCoord * 2).createdDate(now).lastModifiedDate(now).build()
        );

        mongoTemplate.insertAll(initialNewsFacts);
    }
}
