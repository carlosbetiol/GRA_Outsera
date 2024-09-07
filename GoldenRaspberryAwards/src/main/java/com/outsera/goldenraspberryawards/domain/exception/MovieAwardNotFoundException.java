package com.outsera.goldenraspberryawards.domain.exception;

public class MovieAwardNotFoundException extends EntityNotFoundException {

    public MovieAwardNotFoundException(Long id) {
        super(String.format("Movie Award id %d not found.", id));
    }

}
