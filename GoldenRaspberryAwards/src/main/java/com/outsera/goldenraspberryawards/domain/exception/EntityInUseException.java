package com.outsera.goldenraspberryawards.domain.exception;

public class EntityInUseException extends BusinessException {

	public EntityInUseException(String message) {
		super(String.format("The entity is referenced on another database table: %s.", message));
	}
	
}
