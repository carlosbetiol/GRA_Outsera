package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractService {

    private final ContextualRequest contextualRequest;

    public AbstractService(ContextualRequest contextualRequest) {
        this.contextualRequest = contextualRequest;
    }

    @Transactional
    public SysEntity registerLog(SysEntity entity) {
        return contextualRequest.triggerRegisterPersistenceLog(entity);
    }

}
