package com.outsera.goldenraspberryawards.domain.exception;

public class MovieNotFoundException extends EntityNotFoundException {

    public MovieNotFoundException(Long id) {
        super(id.toString());
    }

}
