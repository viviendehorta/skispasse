package vdehorta.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.ap.internal.util.Collections;
import vdehorta.bean.MediaType;
import vdehorta.domain.*;
import vdehorta.bean.dto.NewsFactDetailDto;
import vdehorta.security.RoleEnum;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility Class containing methods that create entities used in controller tests.
 */
public final class BeanTestUtils {

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final double DEFAULT_LOCATION_COORDINATE_X = 100.0;
    public static final double DEFAULT_LOCATION_COORDINATE_Y = 2000.0;
    public static final LocalDateTime DEFAULT_CREATED_DATE = LocalDateTime.parse("2020-04-02T20:15:39");
    public static final LocalDateTime DEFAULT_LAST_MODIFIED_DATE = LocalDateTime.parse("2020-04-03T20:16:39");
    public static final LocalDateTime DEFAULT_EVENT_DATE = LocalDateTime.parse("2020-04-01T00:00:00");
    public static final String DEFAULT_ADDRESS_DETAIL = "address_detail";
    public static final String DEFAULT_CITY = "city";
    public static final String DEFAULT_COUNTRY = "country";
    public static final String DEFAULT_NEWS_FACT_ID = "news_fact_id";
    public static final String DEFAULT_NEWS_CATEGORY_ID = "newsCategoryId";
    public static final String DEFAULT_NEWS_CATEGORY_LABEL = "newsCategoryLabel";
    public static final String DEFAULT_OWNER = "owner";
    public static final String DEFAULT_MEDIA_ID = "mediaId";
    public static final String DEFAULT_MEDIA_CONTENT_TYPE = "mediaContentType";
    public static final String DEFAULT_AUTHORITY_VALUE = RoleEnum.USER.getValue();
    public static final MediaType DEFAULT_MEDIA_TYPE = MediaType.VIDEO;


    public static final String DEFAULT_LOGIN = "johndoe";
    public static final String UPDATED_LOGIN = "updated_login";

    public static final String DEFAULT_USER_ID = "user_id";

    public static final String DEFAULT_PASSWORD = "passjohndoe";
    public static final String UPDATED_PASSWORD = "passSkispasse";

    public static final String DEFAULT_EMAIL = "johndoe@localhost";
    public static final String UPDATED_EMAIL = "updated@localhost";

    public static final String DEFAULT_FIRSTNAME = "john";
    public static final String UPDATED_FIRSTNAME = "updatedFirstName";

    public static final String DEFAULT_LASTNAME = "doe";
    public static final String UPDATED_LASTNAME = "updatedLastName";

    public static final String DEFAULT_ENCODED_PASSWORD = RandomStringUtils.random(60);
    private static final String DEFAULT_EVENT_DATE_STRING = DEFAULT_DATE_FORMATTER.format(DEFAULT_EVENT_DATE);

    public static User createDefaultUser() {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(DEFAULT_ENCODED_PASSWORD);
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);

        Authority authority = new Authority();
        authority.setName(DEFAULT_AUTHORITY_VALUE);
        user.setAuthorities(Collections.asSet(authority));
        return user;
    }

    public static User createDefaultUser1() {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + "1");
        user.setPassword(DEFAULT_ENCODED_PASSWORD.substring(0, DEFAULT_ENCODED_PASSWORD.length() - 1) + "1"); //replace last char of DEFAULT_ENCODED_PASSWORD by 1
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL + "1");
        user.setFirstName(DEFAULT_FIRSTNAME + "1");
        user.setLastName(DEFAULT_LASTNAME + "1");

        Authority authority = new Authority();
        authority.setName(DEFAULT_AUTHORITY_VALUE);
        user.setAuthorities(Collections.asSet(authority));
        return user;
    }

    public static User createDefaultUser2() {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + "2");
        user.setPassword(DEFAULT_ENCODED_PASSWORD.substring(0, DEFAULT_ENCODED_PASSWORD.length() - 1) + "2"); //replace last char of DEFAULT_ENCODED_PASSWORD by 2
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL + "2");
        user.setFirstName(DEFAULT_FIRSTNAME + "2");
        user.setLastName(DEFAULT_LASTNAME + "2");

        Authority authority = new Authority();
        authority.setName(DEFAULT_AUTHORITY_VALUE);
        user.setAuthorities(Collections.asSet(authority));
        return user;
    }

    public static NewsFact createDefaultNewsFact() {
        return new NewsFact.Builder()
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .createdDate(DEFAULT_CREATED_DATE)
                .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
                .eventDate(DEFAULT_EVENT_DATE)
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinateX(DEFAULT_LOCATION_COORDINATE_X)
                .locationCoordinateY(DEFAULT_LOCATION_COORDINATE_Y)
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .newsCategoryLabel(DEFAULT_NEWS_CATEGORY_LABEL)
                .owner(DEFAULT_OWNER)
                .build();
    }

    public static NewsFact createDefaultNewsFact1() {
        return new NewsFact.Builder()
                .addressDetail(DEFAULT_ADDRESS_DETAIL + "1")
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
                .build();
    }

    public static NewsFact createDefaultNewsFact2() {
        return new NewsFact.Builder()
                .addressDetail(DEFAULT_ADDRESS_DETAIL + "2")
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
                .build();
    }

    public static NewsCategory createDefaultNewsCategory() {
        return new NewsCategory.Builder()
                .id(DEFAULT_NEWS_CATEGORY_ID)
                .label(DEFAULT_NEWS_CATEGORY_LABEL)
                .build();
    }

    public static NewsCategory createDefaultNewsCategory1() {
        return new NewsCategory.Builder()
                .id(DEFAULT_NEWS_CATEGORY_ID + "1")
                .label(DEFAULT_NEWS_CATEGORY_LABEL + "1")
                .build();
    }

    public static NewsCategory createDefaultNewsCategory2() {
        return new NewsCategory.Builder()
                .id(DEFAULT_NEWS_CATEGORY_ID + "2")
                .label(DEFAULT_NEWS_CATEGORY_LABEL + "2")
                .build();
    }

    public static NewsFactDetailDto createDefaultNewsFactDetailDto() {
        return new NewsFactDetailDto.Builder()
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .eventDate(DEFAULT_EVENT_DATE_STRING)
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .newsCategoryLabel(DEFAULT_NEWS_CATEGORY_LABEL)
                .build();
    }

    /**
     * Verifies the equals/hashcode contract on the domain object.
     */
    public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
        T domainObject1 = clazz.getConstructor().newInstance();
        assertThat(domainObject1.toString()).isNotNull();
        assertThat(domainObject1).isEqualTo(domainObject1);
        assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());
        // Test with an instance of another class
        Object testOtherObject = new Object();
        assertThat(domainObject1).isNotEqualTo(testOtherObject);
        assertThat(domainObject1).isNotEqualTo(null);
        // Test with an instance of the same class
        T domainObject2 = clazz.getConstructor().newInstance();
        assertThat(domainObject1).isNotEqualTo(domainObject2);
    }


    private BeanTestUtils() {
    }
}
