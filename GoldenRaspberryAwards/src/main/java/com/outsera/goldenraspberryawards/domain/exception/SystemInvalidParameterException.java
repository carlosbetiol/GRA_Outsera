package com.outsera.goldenraspberryawards.domain.exception;

import com.outsera.goldenraspberryawards.core.internationalization.MessageSystem;

public class SystemInvalidParameterException extends BusinessException {

	public SystemInvalidParameterException(String message) {
		super(MessageSystem.getInstance().getLocalizedMessage("exception.customInvalidParameter", new Object[]{message}));
	}
	
}
