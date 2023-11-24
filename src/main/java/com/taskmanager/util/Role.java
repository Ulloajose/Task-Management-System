package com.taskmanager.util;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN(1),
    USER(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }
}
