package com.outsera.goldenraspberryawards.domain.exception;

import com.outsera.goldenraspberryawards.core.internationalization.MessageSystem;

public class EntityInUseException extends BusinessException {

	public EntityInUseException(String message) {
		super(MessageSystem.getInstance().getLocalizedMessage("exception.entityInUse", new Object[]{message}));
	}
	
}
