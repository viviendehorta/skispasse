package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.service.ClockService;
import vdehorta.service.VideoService;
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
public class Migration20200629 {

    @ChangeSet(order = "01", author = "admin", id = "01-replaceInitialNewsFactByRealOnes")
    public void replaceInitialNewsFactByRealOnes(MongoTemplate mongoTemplate, Environment environment) {

        Logger logger = LoggerFactory.getLogger(Migration20200629.class);
        logger.debug("Start migration 'replaceInitialNewsFactByRealOnes'");

        deleteInitialNewsFactsAndVideos(mongoTemplate, environment);

        GridFsTemplate gridFsTemplate = new GridFsTemplate(
                mongoTemplate.getMongoDbFactory(),
                mongoTemplate.getConverter(),
                environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket"));

        ClockService clockService = new ClockService();

        Map<String, NewsFact> newsFactByVideoFileName = buildNewsFactByVideoFilename(mongoTemplate, clockService);

        String baseDirectory = "/home/vivien/Bureau/video/album/skispasse/";

        for (Map.Entry<String, NewsFact> entry : newsFactByVideoFileName.entrySet()) {

            NewsFact savedNewsFact = mongoTemplate.insert(entry.getValue());

            //Persist video
            File videoFile = new File(baseDirectory + entry.getKey());
            String gridFsFilename = savedNewsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "." + FilenameUtils.getExtension(videoFile.getName());

            try (FileInputStream fileInputStream = new FileInputStream(videoFile)) {
                String mediaId = gridFsTemplate.store(
                        fileInputStream,
                        gridFsFilename,
                        new Document().append(VideoService.OWNER_METADATA_KEY, savedNewsFact.getOwner())).toString();

                //Update news fact with video id
                savedNewsFact.setMediaId(mediaId);
                mongoTemplate.save(savedNewsFact);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.debug("End migration 'replaceInitialNewsFactByRealOnes'");
    }

    private void deleteInitialNewsFactsAndVideos(MongoTemplate mongoTemplate, Environment environment) {
        mongoTemplate.dropCollection(NewsFact.class);
        mongoTemplate.dropCollection(environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket") + ".files");
        mongoTemplate.dropCollection(environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket") + ".chunks");
    }

    private Map<String, NewsFact> buildNewsFactByVideoFilename(MongoTemplate mongoTemplate, ClockService clockService) {
        List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);
        Map<String, String> categoryLabelById = allCategories.stream().collect(Collectors.toMap(NewsCategory::getId, NewsCategory::getLabel));

        Map<String, NewsFact> map = new HashMap<>();

        map.put("bondy-pluie-fenetre.mp4",
                createNewsFact("6", categoryLabelById, 0, 0, "2020-04-18", "France", "Bondy", "41 rue de la Liberté, 93140 Bondy", clockService.now()));

        map.put("echirolles-entrainement-confinement.mp4",
                createNewsFact("2", categoryLabelById, 0, 0, "2020-03-26", "France", "Échirolles", "5 rue Louise Michel, 38130 Échirolles", clockService.now()));

        map.put("echirolles-pingpong-interieur.3gp",
                createNewsFact("2", categoryLabelById, 0, 0, "2020-03-19", "France", "Échirolles", "5 rue Louise Michel, 38130 Échirolles", clockService.now()));

        map.put("floripa-lagoa-lever-soleil.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-06-22", "Brasil", "Florianópolis", "Avenida das Rendeiras, Lagoa da Conceição, Florianópolis", clockService.now()));

        map.put("floripa-lagoinha-do-leste-plage.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-03-04", "Brasil", "Florianópolis", "Praia da Lagoinha do Leste, Florianópolis", clockService.now()));

        map.put("floripa-lagoinha-do-leste-sommet.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-03-04", "Brasil", "Florianópolis", "Praia da Lagoinha do Leste, Florianópolis", clockService.now()));

        map.put("floripa-mesures-covid-prefet.mp4",
                createNewsFact("6", categoryLabelById, 0, 0, "2020-03-04", "Brasil", "Florianópolis", "Florianópolis", clockService.now()));

        map.put("floripa-pequi-biere-pression.mp4",
                createNewsFact("6", categoryLabelById, 0, 0, "2020-04-16", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-bistrot-maison.mp4",
                createNewsFact("6", categoryLabelById, 0, 0, "2020-06-14", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-pingpong-bizarre.mp4",
                createNewsFact("2", categoryLabelById, 0, 0, "2020-03-28", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-sao-joao-bouffe.mp4",
                createNewsFact("3", categoryLabelById, 0, 0, "2020-06-25", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-sao-joao-danse.mp4",
                createNewsFact("4", categoryLabelById, 0, 0, "2020-06-25", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-show-live.mp4",
                createNewsFact("4", categoryLabelById, 0, 0, "2020-04-04", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-praiamole-lever-soleil.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-06-22", "Brasil", "Florianópolis", "Praia Mole, Florianópolis", clockService.now()));

        map.put("floripa-praiamole-marche-mains.mp4",
                createNewsFact("2", categoryLabelById, 0, 0, "2020-06-22", "Brasil", "Florianópolis", "Praia Mole, Florianópolis", clockService.now()));

        map.put("lyon-garfield-balcon.mp4",
                createNewsFact("6", categoryLabelById, 0, 0, "2020-05-30", "France", "Lyon", "Lyon", clockService.now()));

        map.put("lyon-randonnee-sauterelles.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-06-27", "France", "Lyon", "Montagnes, Lyon", clockService.now()));

        map.put("pipa-bodysurf.mp4",
                createNewsFact("2", categoryLabelById, 0, 0, "2019-10-15", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-chatons.mp4",
                createNewsFact("6", categoryLabelById, 0, 0, "2020-04-12", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-banane.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-04-07", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-jardin-moto.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-03-05", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-mur.mp4",
                createNewsFact("5", categoryLabelById, 0, 0, "2020-06-02", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-moto.mp4",
                createNewsFact("2", categoryLabelById, 0, 0, "2020-02-04", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-nage-en-mer.mp4",
                createNewsFact("2", categoryLabelById, 0, 0, "2019-10-15", "Brasil", "Pipa", "Praia do Madeiro, Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-terrasse-exterieure.mp4",
                createNewsFact("6", categoryLabelById, 0, 0, "2020-04-15", "Brasil", "Pipa", "Pipa, Rio Grande do Norte", clockService.now()));

        map.put("prague-musee-illusion.mp4",
                createNewsFact("3", categoryLabelById, 0, 0, "2020-05-01", "Czech republic", "Prague", "Illusion Art Museum, Prague", clockService.now()));

        map.put("valpo-singes-de-rue.mp4",
                createNewsFact("1", categoryLabelById, 0, 0, "2019-04-01", "Chile", "Valparaiso", "Calle Prat, Valparaiso", clockService.now()));

        return map;
    }

    private NewsFact createNewsFact(String categoryId, Map<String, String> categoryLabelById, long xCoordinate, long yCoordinate, String eventDate, String country, String city, String address, LocalDateTime now) {
        NewsFact.Builder builder = new NewsFact.Builder();
        return builder
                .newsCategoryId(categoryId)
                .newsCategoryLabel(categoryLabelById.get(categoryId))
                .country(country)
                .city(city)
                .address(address)
                .owner("admin")
                .eventDate(LocalDate.parse(eventDate, DATE_FORMATTER).atStartOfDay())
                .locationCoordinateX(xCoordinate)
                .locationCoordinateY(yCoordinate)
                .createdDate(now)
                .lastModifiedDate(now)
                .build();
    }
}
