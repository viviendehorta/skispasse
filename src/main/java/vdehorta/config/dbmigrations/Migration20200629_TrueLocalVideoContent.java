package vdehorta.config.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.bean.MediaType;
import vdehorta.config.ProfileConstants;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.service.ClockService;
import vdehorta.service.MediaService;
import vdehorta.service.util.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vdehorta.service.util.DateUtil.DATE_FORMATTER;

@ChangeLog(order = "20200629")
@Profile(ProfileConstants.SPRING_PROFILE_LOCAL)
public class Migration20200629_TrueLocalVideoContent {

    @ChangeSet(order = "01", author = "admin", id = "01-addNewsFactWithTrueVideoContent")
    public void addNewsFactWithTrueVideoContent(MongoTemplate mongoTemplate, ClockService clockService, GridFsTemplate mediaGridFsTemplate) {

        Logger logger = LoggerFactory.getLogger(Migration20200629_TrueLocalVideoContent.class);
        logger.debug("Start migration 'replaceInitialNewsFactByRealOnes'");

        Map<String, NewsFact> newsFactByVideoFileName = buildNewsFactByVideoFilename(mongoTemplate, clockService);

        String baseDirectory = "/home/vivien/Bureau/video/album/skispasse/";

        for (Map.Entry<String, NewsFact> entry : newsFactByVideoFileName.entrySet()) {

            NewsFact savedNewsFact = mongoTemplate.insert(entry.getValue());

            //Persist video
            File videoFile = new File(baseDirectory + entry.getKey());
            ContentTypeEnum contentTypeEnum = ContentTypeEnum.MP4;
            String gridFsFilename = savedNewsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "_" + contentTypeEnum.name();

            try (FileInputStream fileInputStream = new FileInputStream(videoFile)) {
                String videoId = mediaGridFsTemplate.store(
                        fileInputStream,
                        gridFsFilename,
                        contentTypeEnum.getContentType(),
                        new Document().append(MediaService.OWNER_METADATA_KEY, savedNewsFact.getOwner())).toString();

                //Update news fact with video id
                savedNewsFact.setMediaId(videoId);
                mongoTemplate.save(savedNewsFact);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.debug("End migration 'replaceInitialNewsFactByRealOnes'");
    }

    private Map<String, NewsFact> buildNewsFactByVideoFilename(MongoTemplate mongoTemplate, ClockService clockService) {
        List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);
        Map<String, String> categoryLabelById = allCategories.stream().collect(Collectors.toMap(NewsCategory::getId, NewsCategory::getLabel));

        Map<String, NewsFact> map = new HashMap<>();

        map.put("bondy-pluie-fenetre.mp4",
                createNewsFact("6", categoryLabelById, 48.89452715380395, 2.4816319801042863, "2020-04-18", "France", "Bondy", "41 rue de la Liberté, 93140 Bondy", clockService.now()));

        map.put("echirolles-entrainement-confinement.mp4",
                createNewsFact("2", categoryLabelById, 45.1423551339428, 5.70183361825444, "2020-03-26", "France", "Échirolles", "5 rue Louise Michel, 38130 Échirolles", clockService.now()));

        map.put("floripa-lagoa-lever-soleil.mp4",
                createNewsFact("5", categoryLabelById, -27.60643369713474, -48.46002601356355, "2020-06-22", "Brasil", "Florianópolis", "Avenida das Rendeiras, Lagoa da Conceição, Florianópolis", clockService.now()));

        map.put("floripa-lagoinha-do-leste-plage.mp4",
                createNewsFact("5", categoryLabelById, -27.777719938778475, -48.48820066954825, "2020-03-04", "Brasil", "Florianópolis", "Praia da Lagoinha do Leste, Florianópolis", clockService.now()));

        map.put("floripa-lagoinha-do-leste-sommet.mp4",
                createNewsFact("5", categoryLabelById, -27.781162307577766, -48.488007831454766, "2020-03-04", "Brasil", "Florianópolis", "Praia da Lagoinha do Leste, Florianópolis", clockService.now()));

        map.put("floripa-mesures-covid-prefet.mp4",
                createNewsFact("6", categoryLabelById, -27.590572111477698, -48.56128656829762, "2020-03-04", "Brasil", "Florianópolis", "Florianópolis", clockService.now()));

        map.put("floripa-pequi-biere-pression.mp4",
                createNewsFact("6", categoryLabelById, -27.60535872610717, -48.467523794994186, "2020-04-16", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-bistrot-maison.mp4",
                createNewsFact("6", categoryLabelById, -27.605349218638764, -48.467547398564484, "2020-06-14", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-pingpong-bizarre.mp4",
                createNewsFact("2", categoryLabelById, -27.60539485450856, -48.467622500416894, "2020-03-28", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-sao-joao-bouffe.mp4",
                createNewsFact("3", categoryLabelById, -27.605383445542905, -48.467560273135014, "2020-06-25", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-sao-joao-danse.mp4",
                createNewsFact("4", categoryLabelById, -27.605275060364214, -48.467609625682655, "2020-06-25", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-show-live.mp4",
                createNewsFact("4", categoryLabelById, -27.60533780966834, -48.467547398564484, "2020-04-04", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-praiamole-lever-soleil.mp4",
                createNewsFact("5", categoryLabelById, -27.603495722092084, -48.433531624328744, "2020-06-22", "Brasil", "Florianópolis", "Praia Mole, Florianópolis", clockService.now()));

        map.put("floripa-praiamole-marche-mains.mp4",
                createNewsFact("2", categoryLabelById, -27.602484105651584, -48.43293939244729, "2020-06-22", "Brasil", "Florianópolis", "Praia Mole, Florianópolis", clockService.now()));

        map.put("lyon-garfield-balcon.mp4",
                createNewsFact("6", categoryLabelById, 45.75781459741205, 4.832434073523939, "2020-05-30", "France", "Lyon", "Lyon", clockService.now()));

        map.put("lyon-randonnee-sauterelles.mp4",
                createNewsFact("5", categoryLabelById, 45.795684008208326, 4.887760543697837, "2020-06-27", "France", "Lyon", "Montagnes, Lyon", clockService.now()));

        map.put("pipa-bodysurf.mp4",
                createNewsFact("2", categoryLabelById, -6.226965621833102, -35.06093235596418, "2019-10-15", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-chatons.mp4",
                createNewsFact("6", categoryLabelById, -6.236107212005578, -35.05686588311181, "2020-04-12", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-banane.mp4",
                createNewsFact("5", categoryLabelById, -6.235971762172426, -35.05644102081097, "2020-04-07", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-jardin-moto.mp4",
                createNewsFact("5", categoryLabelById, -6.235856576313267, -35.05657834978157, "2020-03-05", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-mur.mp4",
                createNewsFact("5", categoryLabelById, -6.2360656171414774, -35.056595515984746, "2020-06-02", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-moto.mp4",
                createNewsFact("2", categoryLabelById, -6.201118365970501, -35.08205718133943, "2020-02-04", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-nage-en-mer.mp4",
                createNewsFact("2", categoryLabelById, -6.224173617864764, -35.05671995843407, "2019-10-15", "Brasil", "Pipa", "Praia do Madeiro, Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-terrasse-exterieure.mp4",
                createNewsFact("6", categoryLabelById, -6.235856576313267, -35.05678434356488, "2020-04-15", "Brasil", "Pipa", "Pipa, Rio Grande do Norte", clockService.now()));

        map.put("prague-musee-illusion.mp4",
                createNewsFact("3", categoryLabelById, 50.087215709292536, 14.419828132899731, "2020-05-01", "Czech republic", "Prague", "Illusion Art Museum, Prague", clockService.now()));

        map.put("valpo-singes-de-rue.mp4",
                createNewsFact("1", categoryLabelById, -33.040570108374624, -71.62676924124294, "2019-04-01", "Chile", "Valparaiso", "Calle Prat, Valparaiso", clockService.now()));

        return map;
    }

    private NewsFact createNewsFact(String categoryId, Map<String, String> categoryLabelById, double xCoordinate, double yCoordinate, String eventDate, String country, String city, String address, LocalDateTime now) {
        NewsFact.Builder builder = new NewsFact.Builder();
        return builder
                .newsCategoryId(categoryId)
                .newsCategoryLabel(categoryLabelById.get(categoryId))
                .country(country)
                .city(city)
                .addressDetail(address)
                .owner("admin")
                .eventDate(LocalDate.parse(eventDate, DATE_FORMATTER).atStartOfDay())
                .locationLatitude(xCoordinate)
                .locationLongitude(yCoordinate)
                .createdDate(now)
                .lastModifiedDate(now)
                .mediaType(MediaType.VIDEO.name())
                .mediaContentType(ContentTypeEnum.MP4.getContentType())
                .build();
    }
}
