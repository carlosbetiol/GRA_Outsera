package com.outsera.goldenraspberryawards.domain.exception;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Long id) {
        super(id.toString());
    }

    public UserNotFoundException(String id) {
        super(id);
    }

}
