package vdehorta.web.rest;

import org.apache.commons.lang3.RandomStringUtils;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;

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


    /**
     * Create a User.
     */
    public static User createUser() {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    /**
     * Create a News Fact entity with all fields initialized with fixed value, and string fields suffixed by the
     * given suffix.
     *
     * @param suffix the suffix to add at the end of NewsFact fields
     * @return The NewsFact created
     */
    public static NewsFact createSuffixFieldNewsFact(String suffix) {
        return new NewsFact.Builder()
            .address(DEFAULT_ADDRESS + suffix)
            .city(DEFAULT_CITY + suffix)
            .country(DEFAULT_COUNTRY + suffix)
            .createdDate(DEFAULT_CREATED_DATE)
            .eventDate(DEFAULT_EVENT_DATE)
            .id(DEFAULT_NEWS_FACT_ID + suffix)
            .locationCoordinateX(DEFAULT_LOCATION_COORDINATE_X)
            .locationCoordinateY(DEFAULT_LOCATION_COORDINATE_Y)
            .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID + suffix)
            .newsCategoryLabel(DEFAULT_NEWS_CATEGORY_LABEL + suffix)
            .owner(DEFAULT_OWNER + suffix)
            .videoPath(DEFAULT_VIDEO_PATH + suffix)
            .build();
    }

    public static NewsFact createNewsFact() {
        return createSuffixFieldNewsFact("");
    }


    private EntityTestUtil() {
    }
}
