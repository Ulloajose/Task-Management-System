package com.taskmanager.domain.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableTaskDto {

    private Long id;
    private String name;
    private String description;
    private String dueDate;
    private String createDate;
    private String lastModifiedDate;
    private String taskStatus;
}
