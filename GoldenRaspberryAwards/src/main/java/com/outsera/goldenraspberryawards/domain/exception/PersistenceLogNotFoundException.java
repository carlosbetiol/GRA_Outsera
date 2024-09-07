package com.outsera.goldenraspberryawards.domain.exception;

public class PersistenceLogNotFoundException extends EntityNotFoundException {

    public PersistenceLogNotFoundException(Long id) {
        super(String.format("Persistence log id %d not found.", id));
    }

}
