package com.outsera.goldenraspberryawards.domain.listener;

import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;

public class PersistenceLogRegisterEvent {

    private final PersistenceLog persistenceLog;

    public PersistenceLogRegisterEvent(PersistenceLog persistenceLog) {
        this.persistenceLog = persistenceLog;
    }

    public PersistenceLog getPersistenceLog() {
        return persistenceLog;
    }

}
