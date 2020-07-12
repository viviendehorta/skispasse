package vdehorta.config.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.config.ProfileConstants;
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
@Profile(ProfileConstants.SPRING_PROFILE_LOCAL)
public class Migration20200629_TrueLocalVideoContent {

    @ChangeSet(order = "01", author = "admin", id = "01-addNewsFactWithTrueVideoContent")
    public void addNewsFactWithTrueVideoContent(MongoTemplate mongoTemplate, Environment environment, ClockService clockService, GridFsTemplate videoGridFsTemplate) {

        Logger logger = LoggerFactory.getLogger(Migration20200629_TrueLocalVideoContent.class);
        logger.debug("Start migration 'replaceInitialNewsFactByRealOnes'");

        Map<String, NewsFact> newsFactByVideoFileName = buildNewsFactByVideoFilename(mongoTemplate, clockService);

        String baseDirectory = "/home/vivien/Bureau/video/album/skispasse/";

        for (Map.Entry<String, NewsFact> entry : newsFactByVideoFileName.entrySet()) {

            NewsFact savedNewsFact = mongoTemplate.insert(entry.getValue());

            //Persist video
            File videoFile = new File(baseDirectory + entry.getKey());
            ContentTypeEnum contentTypeEnum = ContentTypeEnum.MP4;
            String gridFsFilename = savedNewsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "." + contentTypeEnum.getExtension();

            try (FileInputStream fileInputStream = new FileInputStream(videoFile)) {
                String mediaId = videoGridFsTemplate.store(
                        fileInputStream,
                        gridFsFilename,
                        contentTypeEnum.getContentType(),
                        new Document().append(VideoService.OWNER_METADATA_KEY, savedNewsFact.getOwner())).toString();

                //Update news fact with video id
                savedNewsFact.setMediaId(mediaId);
                savedNewsFact.setMediaContentType(contentTypeEnum.getContentType());
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
                createNewsFact("6", categoryLabelById, 276254.0083615124,6256983.7713107485, "2020-04-18", "France", "Bondy", "41 rue de la Liberté, 93140 Bondy", clockService.now()));

        map.put("echirolles-entrainement-confinement.mp4",
                createNewsFact("2", categoryLabelById, 634725.214972053,5643960.29855006, "2020-03-26", "France", "Échirolles", "5 rue Louise Michel, 38130 Échirolles", clockService.now()));

        map.put("floripa-lagoa-lever-soleil.mp4",
                createNewsFact("5", categoryLabelById, -5394545.419658685,-3199444.088359589, "2020-06-22", "Brasil", "Florianópolis", "Avenida das Rendeiras, Lagoa da Conceição, Florianópolis", clockService.now()));

        map.put("floripa-lagoinha-do-leste-plage.mp4",
                createNewsFact("5", categoryLabelById, -5397681.808016178,-3220978.153671176, "2020-03-04", "Brasil", "Florianópolis", "Praia da Lagoinha do Leste, Florianópolis", clockService.now()));

        map.put("floripa-lagoinha-do-leste-sommet.mp4",
                createNewsFact("5", categoryLabelById, -5397660.341377806,-3221411.2743182266, "2020-03-04", "Brasil", "Florianópolis", "Praia da Lagoinha do Leste, Florianópolis", clockService.now()));

        map.put("floripa-mesures-covid-prefet.mp4",
                createNewsFact("6", categoryLabelById, -5405817.693049126,-3197451.6798769743, "2020-03-04", "Brasil", "Florianópolis", "Florianópolis", clockService.now()));

        map.put("floripa-pequi-biere-pression.mp4",
                createNewsFact("6", categoryLabelById, -5395380.068869623,-3199309.0497870944, "2020-04-16", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-bistrot-maison.mp4",
                createNewsFact("6", categoryLabelById, -5395382.69640705,-3199307.855458528, "2020-06-14", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-pingpong-bizarre.mp4",
                createNewsFact("2", categoryLabelById, -5395391.056707018,-3199313.588239294, "2020-03-28", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-sao-joao-bouffe.mp4",
                createNewsFact("3", categoryLabelById, -5395384.129597685,-3199312.155044102, "2020-06-25", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-sao-joao-danse.mp4",
                createNewsFact("4", categoryLabelById, -5395389.6234981585,-3199298.5396966166, "2020-06-25", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-pequi-show-live.mp4",
                createNewsFact("4", categoryLabelById, -5395382.69640705,-3199306.422263336, "2020-04-04", "Brasil", "Florianópolis", "Pequi Hostel, Rua José Henrique Veras, 203 - Lagoa da Conceição, Florianópolis - SC, 88062-030", clockService.now()));

        map.put("floripa-praiamole-lever-soleil.mp4",
                createNewsFact("5", categoryLabelById, -5391596.077740189,-3199075.0211262708, "2020-06-22", "Brasil", "Florianópolis", "Praia Mole, Florianópolis", clockService.now()));

        map.put("floripa-praiamole-marche-mains.mp4",
                createNewsFact("2", categoryLabelById, -5391530.150788713,-3198947.9445694806, "2020-06-22", "Brasil", "Florianópolis", "Praia Mole, Florianópolis", clockService.now()));

        map.put("lyon-garfield-balcon.mp4",
                createNewsFact("6", categoryLabelById, 537944.1003567497,5741623.416527158, "2020-05-30", "France", "Lyon", "Lyon", clockService.now()));

        map.put("lyon-randonnee-sauterelles.mp4",
                createNewsFact("5", categoryLabelById, 544103.0148438972,5747667.674451335, "2020-06-27", "France", "Lyon", "Montagnes, Lyon", clockService.now()));

        map.put("pipa-bodysurf.mp4",
                createNewsFact("2", categoryLabelById, -3902965.1366033424,-694551.2816115234, "2019-10-15", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-chatons.mp4",
                createNewsFact("6", categoryLabelById, -3902512.458916092,-695574.9673570073, "2020-04-12", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-banane.mp4",
                createNewsFact("5", categoryLabelById, -3902465.163461105,-695559.7993992416, "2020-04-07", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-jardin-moto.mp4",
                createNewsFact("5", categoryLabelById, -3902480.450852183,-695546.9006470736, "2020-03-05", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-macaque-mur.mp4",
                createNewsFact("5", categoryLabelById, -3902482.36178518,-695570.3094760515, "2020-06-02", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-moto.mp4",
                createNewsFact("2", categoryLabelById, -3905316.7414072114,-691656.9726138415, "2020-02-04", "Brasil", "Pipa", "Praia da Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-nage-en-mer.mp4",
                createNewsFact("2", categoryLabelById, -3902496.2146552713,-694238.6333608079, "2019-10-15", "Brasil", "Pipa", "Praia do Madeiro, Pipa, Rio Grande do Norte", clockService.now()));

        map.put("pipa-terrasse-exterieure.mp4",
                createNewsFact("6", categoryLabelById, -3902503.381975248,-695546.9006470736, "2020-04-15", "Brasil", "Pipa", "Pipa, Rio Grande do Norte", clockService.now()));

        map.put("prague-musee-illusion.mp4",
                createNewsFact("3", categoryLabelById, 1605207.925080919,6461393.788134341, "2020-05-01", "Czech republic", "Prague", "Illusion Art Museum, Prague", clockService.now()));

        map.put("valpo-singes-de-rue.mp4",
                createNewsFact("1", categoryLabelById, -7973455.479102474,-3900690.2056625187, "2019-04-01", "Chile", "Valparaiso", "Calle Prat, Valparaiso", clockService.now()));

        return map;
    }

    private NewsFact createNewsFact(String categoryId, Map<String, String> categoryLabelById, double xCoordinate, double yCoordinate, String eventDate, String country, String city, String address, LocalDateTime now) {
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
