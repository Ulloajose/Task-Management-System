package com.taskmanager.domain.dto.task;

import com.taskmanager.controller.constraint.DateConstraint;
import com.taskmanager.util.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {

    @NotBlank
    private String name;
    private String description;
    @DateConstraint
    private String dueDate;
    private TaskStatus taskStatus;
}
