package com.taskmanager.domain.exeption;

import java.io.Serial;

public class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message, new RuntimeException(message));
    }
}
