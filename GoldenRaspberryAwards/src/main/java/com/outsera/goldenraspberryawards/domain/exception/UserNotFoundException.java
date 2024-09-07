package com.outsera.goldenraspberryawards.domain.exception;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Long id) {
        super(String.format("User id %d not found.", id));
    }

    public UserNotFoundException(String id) {
        super(String.format("User %s not found.", id));
    }

}
