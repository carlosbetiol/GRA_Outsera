package com.outsera.goldenraspberryawards.domain.exception;

public class StudioNotFoundException extends EntityNotFoundException {

    public StudioNotFoundException(Long id) {
        super(id.toString());
    }

}
