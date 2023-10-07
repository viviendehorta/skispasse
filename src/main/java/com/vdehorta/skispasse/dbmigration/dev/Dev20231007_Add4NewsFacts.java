package com.vdehorta.skispasse.dbmigration.dev;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Profile({"dev"})
@ChangeUnit(id = "Dev20230409_1_AddActors", order = "1", author = "mongock")
public class Dev20231007_Add4NewsFacts {

    public Dev20231007_Add4NewsFacts() {
    }

    @Execution
    public void changeSet() {

        Map<String, NewsFact> newsFactByVideoFileName = buildNewsFactByVideoFilename();

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
    }

    @RollbackExecution
    public void rollback() {
        //NOT IMPLEMENTED UNTIL MIGRATION ROLLBACK STRATEGY IS DECIDED
        //NOT NECESSARY FOR MIGRATION BECAUSE TRANSACTIONS ARE ENABLED
    }
}
