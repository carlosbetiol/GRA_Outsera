package com.outsera.goldenraspberryawards.domain.exception;

import com.outsera.goldenraspberryawards.core.internationalization.MessageSystem;

public class MovieAwardNotFoundException extends EntityNotFoundException {

    public MovieAwardNotFoundException(Long id) {
        super(id.toString());
    }

}
