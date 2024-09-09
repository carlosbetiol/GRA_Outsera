package com.outsera.goldenraspberryawards.domain.initializer;

import com.outsera.goldenraspberryawards.core.database.DatabaseProperties;
import com.outsera.goldenraspberryawards.core.helper.CSVHelper;
import com.outsera.goldenraspberryawards.domain.service.DatabasePopulateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;

@Component
@Log4j2
public class EntitiesInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final DatabaseProperties databaseProperties;

    private final DatabasePopulateService databasePopulateService;

    public EntitiesInitializer(DatabaseProperties databaseProperties, DatabasePopulateService databasePopulateService) {
        this.databaseProperties = databaseProperties;
        this.databasePopulateService = databasePopulateService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("Initializing Entities from CSV file");

        List<Map<String,Object>> dataFromCsv = new ArrayList<>();

        if(isNull(databaseProperties.getCsvFilePath())) {
            log.error("CSV file path not found in application.properties");
            return;
        }

        try {
            dataFromCsv = CSVHelper.readCSV(databaseProperties.getCsvFilePath(), ";", null,
                    Set.of("year", "title", "studios", "producers", "winner"));
        } catch (IOException e) {
            log.error("Error reading CSV file: {}", e.getMessage());
        }

        List<Map<String, Set<Object>>> parsedData = CSVHelper.parseData(dataFromCsv, Map.of(
                "studios", ",|,and\\s|\\sand\\s",
                "producers", ",|,and\\s|\\sand\\s"
                )
        );

        databasePopulateService.populateEntities(parsedData);

        log.info("Entities initialized from CSV file");

    }

}
