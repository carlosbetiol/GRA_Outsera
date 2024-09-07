package com.outsera.goldenraspberryawards.domain.exception;

import com.outsera.goldenraspberryawards.core.internationalization.MessageSystem;

public abstract class EntityNotFoundException extends BusinessException {

	public EntityNotFoundException(String message) {
		super(MessageSystem.getInstance().getLocalizedMessage("exception.resourceNotFound", new Object[]{message}));
	}
	
}
