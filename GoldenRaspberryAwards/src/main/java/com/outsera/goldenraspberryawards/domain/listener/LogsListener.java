package com.outsera.goldenraspberryawards.domain.listener;

import com.outsera.goldenraspberryawards.domain.service.PersistenceLogService;
import com.outsera.goldenraspberryawards.domain.service.RequestLogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LogsListener {

    private final RequestLogService requestLogService;
    private final PersistenceLogService persistenceLogService;

    public LogsListener(RequestLogService requestLogService, PersistenceLogService persistenceLogService) {
        this.requestLogService = requestLogService;
        this.persistenceLogService = persistenceLogService;
    }

    @EventListener
    public void requestLogRegisterListener(RequestLogRegisterEvent event) {
        requestLogService.save(event.getRequestLog());
    }

    @EventListener
    public void syslogRegisterListener(PersistenceLogRegisterEvent event) {
        persistenceLogService.save(event.getPersistenceLog());
    }

}
