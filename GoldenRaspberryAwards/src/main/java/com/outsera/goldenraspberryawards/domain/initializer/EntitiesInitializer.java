package com.outsera.goldenraspberryawards.domain.initializer;

import com.outsera.goldenraspberryawards.domain.service.DatabasePopulateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@Profile({"dev", "prod"})
public class EntitiesInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final DatabasePopulateService databasePopulateService;

    public EntitiesInitializer(DatabasePopulateService databasePopulateService) {
        this.databasePopulateService = databasePopulateService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        databasePopulateService.populateEntities();

    }

}
