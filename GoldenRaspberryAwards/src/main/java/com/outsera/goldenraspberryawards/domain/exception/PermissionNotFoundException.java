package com.outsera.goldenraspberryawards.domain.exception;

public class PermissionNotFoundException extends EntityNotFoundException {

    public PermissionNotFoundException(Long id) {
        super(id.toString());
    }

    public PermissionNotFoundException(String id) {
        super(id);
    }

}
