package com.outsera.goldenraspberryawards.domain.exception;

public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException(Long id) {
        super(id.toString());
    }

    public RoleNotFoundException(String id) {
        super(id);
    }

}
