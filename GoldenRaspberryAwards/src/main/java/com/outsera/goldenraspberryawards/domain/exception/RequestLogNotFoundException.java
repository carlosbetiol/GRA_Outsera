package com.outsera.goldenraspberryawards.domain.exception;

public class RequestLogNotFoundException extends EntityNotFoundException {

    public RequestLogNotFoundException(Long id) {
        super(id.toString());
    }

}
