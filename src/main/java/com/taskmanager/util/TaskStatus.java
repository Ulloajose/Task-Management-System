package com.taskmanager.util;

import lombok.Getter;

@Getter
public enum TaskStatus {
    PENDING(1),
    IN_PROGRESS(2),
    COMPLETED(3),
    CANCELLED(4);

    private final int id;

    TaskStatus(int id) {
        this.id = id;
    }
}
