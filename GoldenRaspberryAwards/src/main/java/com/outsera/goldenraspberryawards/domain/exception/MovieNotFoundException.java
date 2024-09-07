package com.outsera.goldenraspberryawards.domain.exception;

public class MovieNotFoundException extends EntityNotFoundException {

    public MovieNotFoundException(Long id) {
        super(String.format("Movie id %d not found.", id));
    }

}
