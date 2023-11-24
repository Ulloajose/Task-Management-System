package com.taskmanager.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailResponse {

    private String internalCode;
    private String message;
    private String detail;
}
