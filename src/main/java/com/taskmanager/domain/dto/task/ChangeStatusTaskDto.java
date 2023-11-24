package com.taskmanager.domain.dto.task;

import com.taskmanager.util.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusTaskDto {

    @NotNull
    private TaskStatus taskStatus;
}
