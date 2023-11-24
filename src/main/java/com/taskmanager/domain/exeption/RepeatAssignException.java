package com.taskmanager.domain.exeption;

import java.io.Serial;

public class RepeatAssignException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RepeatAssignException(String message) {
        super(message);
    }
}
