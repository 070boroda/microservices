package com.zelenko.restservice.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Integer id) {
        super("Could not find " + id);
    }
}
