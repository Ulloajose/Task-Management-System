package com.taskmanager.domain.exeption;

import java.io.Serial;

public class TaskOverdueException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TaskOverdueException(String message) {
        super(message);
    }
}
