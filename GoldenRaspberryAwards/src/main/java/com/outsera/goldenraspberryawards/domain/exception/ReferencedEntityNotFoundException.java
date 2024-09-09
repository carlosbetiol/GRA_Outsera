package com.outsera.goldenraspberryawards.domain.exception;

import com.outsera.goldenraspberryawards.core.internationalization.MessageSystem;

public class ReferencedEntityNotFoundException extends BusinessException {

	public ReferencedEntityNotFoundException(String message) {
		super(MessageSystem.getInstance().getLocalizedMessage("exception.referencedResourceNotFound", new Object[]{message}));
	}
	
}
