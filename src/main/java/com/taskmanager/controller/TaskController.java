package com.taskmanager.controller;

import com.taskmanager.domain.dto.task.*;
import com.taskmanager.domain.model.DataTablesResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.service.TaskAssignmentService;
import com.taskmanager.domain.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskAssignmentService taskAssignmentService;

    @PostMapping("/task")
    @ResponseStatus(OK)
    public GenericResponse<Void> createTask(@Valid @RequestBody CreateTaskDto dto, Principal principal){
        return taskService.create(dto, principal);
    }

    @GetMapping("/tasks")
    @ResponseStatus(OK)
    public GenericResponse<DataTablesResponse<TableTaskDto>> findAll(
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        return taskService.findAll(pageNumber, pageSize, sort);
    }

    @DeleteMapping("/task/{taskId}")
    @ResponseStatus(OK)
    public GenericResponse<Void> deleteTask(@PathVariable Long taskId, Principal principal){
        return taskService.delete(taskId, principal);
    }

    @PutMapping("/task/{taskId}")
    @ResponseStatus(OK)
    public GenericResponse<Void> updateTask(
            @PathVariable Long taskId, @Valid @RequestBody UpdateTaskDto dto, Principal principal){
        return taskService.update(taskId, dto, principal);
    }

    @PostMapping("/task/{taskId}/assign-task")
    @ResponseStatus(OK)
    public GenericResponse<Void> assignTask(
            @PathVariable Long taskId, @Valid @RequestBody AssignTaskDto dto,
            Principal principal){
        return taskAssignmentService.assignTask(taskId, dto, principal);
    }

    @PostMapping("/task/{taskId}/change-status")
    @ResponseStatus(OK)
    public GenericResponse<Void> changeStatus(
            @PathVariable Long taskId, @Valid @RequestBody ChangeStatusTaskDto dto,
            Principal principal){
        return taskService.changeStatus(taskId, dto, principal);
    }
}
