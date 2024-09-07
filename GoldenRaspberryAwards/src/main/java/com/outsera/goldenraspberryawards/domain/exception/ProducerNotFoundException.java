package com.outsera.goldenraspberryawards.domain.exception;

public class ProducerNotFoundException extends EntityNotFoundException {

    public ProducerNotFoundException(Long id) {
        super(String.format("Producer id %d not found.", id));
    }

}
