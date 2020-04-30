package vdehorta;

import org.apache.commons.lang3.RandomStringUtils;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;
import vdehorta.service.ClockService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Utility Class containing methods that create entities used in controller tests.
 */
public final class EntityTestUtil {

    public static final Long DEFAULT_LOCATION_COORDINATE_X = 100L;
    public static final Long DEFAULT_LOCATION_COORDINATE_Y = 2000L;
    public static final Instant DEFAULT_CREATED_DATE = LocalDateTime.parse("2020-04-02T20:15:39").toInstant(ZoneOffset.UTC);
    public static final Instant DEFAULT_LAST_MODIFIED_DATE = LocalDateTime.parse("2020-04-03T20:16:39").toInstant(ZoneOffset.UTC);
    public static final Instant DEFAULT_EVENT_DATE = LocalDateTime.parse("2020-04-01T00:00:00").toInstant(ZoneOffset.UTC);
    public static final String DEFAULT_ADDRESS = "address";
    public static final String DEFAULT_CITY = "city";
    public static final String DEFAULT_COUNTRY = "country";
    public static final String DEFAULT_NEWS_FACT_ID = "news_fact_id";
    public static final String DEFAULT_NEWS_CATEGORY_ID = "newsCategoryId";
    public static final String DEFAULT_NEWS_CATEGORY_LABEL = "newsCategoryLabel";
    public static final String DEFAULT_OWNER = "owner";
    public static final String DEFAULT_VIDEO_PATH = "videoPath";


    public static final String DEFAULT_LOGIN = "johndoe";
    public static final String UPDATED_LOGIN = "jhipster";

    public static final String DEFAULT_USER_ID = "user_id";

    public static final String DEFAULT_PASSWORD = "passjohndoe";
    public static final String UPDATED_PASSWORD = "passjhipster";

    public static final String DEFAULT_EMAIL = "johndoe@localhost";
    public static final String UPDATED_EMAIL = "jhipster@localhost";

    public static final String DEFAULT_FIRSTNAME = "john";
    public static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    public static final String DEFAULT_LASTNAME = "doe";
    public static final String UPDATED_LASTNAME = "jhipsterLastName";

    public static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    public static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    public static final String DEFAULT_LANGKEY = "en";
    public static final String UPDATED_LANGKEY = "fr";

    public static final String DEFAULT_ENCODED_PASSWORD = RandomStringUtils.random(60);

    private static final ClockService CLOCK_SERVICE = new ClockService();


    public static User createDefaultUser() {
        return createDefaultUser(CLOCK_SERVICE.now());
    }

    public static User createDefaultUser1() {
        return createDefaultUser1(CLOCK_SERVICE.now());
    }

    public static User createDefaultUser2() {
        return createDefaultUser2(CLOCK_SERVICE.now());
    }

    public static NewsFact createDefaultNewsFact1() {
        return createDefaultNewsFact1(CLOCK_SERVICE.now());
    }

    public static NewsFact createDefaultNewsFact2() {
        return createDefaultNewsFact2(CLOCK_SERVICE.now());
    }

    public static User createDefaultUser(Instant now) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(DEFAULT_ENCODED_PASSWORD);
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    public static User createDefaultUser1(Instant now) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + "1");
        user.setPassword(DEFAULT_ENCODED_PASSWORD.substring(0, DEFAULT_ENCODED_PASSWORD.length() - 1) + "1"); //replace last char of DEFAULT_ENCODED_PASSWORD by 1
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL + "1");
        user.setFirstName(DEFAULT_FIRSTNAME + "1");
        user.setLastName(DEFAULT_LASTNAME + "1");
        user.setImageUrl(DEFAULT_IMAGEURL + "1");
        user.setLangKey(DEFAULT_LANGKEY + "1");
        return user;
    }

    public static User createDefaultUser2(Instant now) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + "2");
        user.setPassword(DEFAULT_ENCODED_PASSWORD.substring(0, DEFAULT_ENCODED_PASSWORD.length() - 1) + "2"); //replace last char of DEFAULT_ENCODED_PASSWORD by 2
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL + "2");
        user.setFirstName(DEFAULT_FIRSTNAME + "2");
        user.setLastName(DEFAULT_LASTNAME + "2");
        user.setImageUrl(DEFAULT_IMAGEURL + "2");
        user.setLangKey(DEFAULT_LANGKEY + "2");
        return user;
    }

    public static NewsFact createDefaultNewsFact1(Instant now) {
        return new NewsFact.Builder()
            .address(DEFAULT_ADDRESS + "1")
            .city(DEFAULT_CITY + "1")
            .country(DEFAULT_COUNTRY + "1")
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .eventDate(DEFAULT_EVENT_DATE)
            .id(DEFAULT_NEWS_FACT_ID + "1")
            .locationCoordinateX(DEFAULT_LOCATION_COORDINATE_X)
            .locationCoordinateY(DEFAULT_LOCATION_COORDINATE_Y)
            .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID + "1")
            .newsCategoryLabel(DEFAULT_NEWS_CATEGORY_LABEL + "1")
            .owner(DEFAULT_OWNER + "1")
            .videoPath(DEFAULT_VIDEO_PATH + "1")
            .build();
    }

    public static NewsFact createDefaultNewsFact2(Instant now) {
        return new NewsFact.Builder()
            .address(DEFAULT_ADDRESS + "2")
            .city(DEFAULT_CITY + "2")
            .country(DEFAULT_COUNTRY + "2")
            .createdDate(DEFAULT_CREATED_DATE.plus(Duration.ofDays(1)))
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE.plus(Duration.ofDays(1)))
            .eventDate(DEFAULT_EVENT_DATE.plus(Duration.ofDays(1)))
            .id(DEFAULT_NEWS_FACT_ID + "2")
            .locationCoordinateX(DEFAULT_LOCATION_COORDINATE_X)
            .locationCoordinateY(DEFAULT_LOCATION_COORDINATE_Y)
            .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID + "2")
            .newsCategoryLabel(DEFAULT_NEWS_CATEGORY_LABEL + "2")
            .owner(DEFAULT_OWNER + "2")
            .videoPath(DEFAULT_VIDEO_PATH + "2")
            .build();
    }

    public static NewsCategory createDefaultCategory1() {
        return new NewsCategory.Builder()
            .id(DEFAULT_NEWS_CATEGORY_ID + "1")
            .label(DEFAULT_NEWS_CATEGORY_LABEL + "1")
            .build();
    }

    public static NewsCategory createDefaultCategory2() {
        return new NewsCategory.Builder()
            .id(DEFAULT_NEWS_CATEGORY_ID + "2")
            .label(DEFAULT_NEWS_CATEGORY_LABEL + "2")
            .build();
    }


    private EntityTestUtil() {
    }
}
