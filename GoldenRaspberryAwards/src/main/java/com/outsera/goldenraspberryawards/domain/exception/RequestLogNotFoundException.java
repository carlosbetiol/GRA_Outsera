package com.outsera.goldenraspberryawards.domain.exception;

public class RequestLogNotFoundException extends EntityNotFoundException {

    public RequestLogNotFoundException(Long id) {
        super(String.format("Request log id %d not found.", id));
    }

}
