package com.taskmanager.domain.service;

import com.taskmanager.domain.dto.task.AssignTaskDto;
import com.taskmanager.domain.entity.TaskAssignmentEntity;
import com.taskmanager.domain.entity.TaskEntity;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.domain.exeption.RepeatAssignException;
import com.taskmanager.domain.exeption.TaskOverdueException;
import com.taskmanager.domain.mapper.GenericResponseMapper;
import com.taskmanager.domain.mapper.TaskMapper;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.repository.TaskAssignmentRepository;
import com.taskmanager.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskAssignmentService {

    private final UserService userService;
    private final TaskService taskService;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public GenericResponse<Void> assignTask(Long id, AssignTaskDto taskDto, Principal principal){
        UserEntity userAuthenticated = userService.findByUsername(principal.getName());
        TaskEntity taskEntity = taskService.findByIdActive(id);
        UserEntity userEntity = userService.findByIdActive(taskDto.getUserId());

        Optional<TaskAssignmentEntity> optional = taskAssignmentRepository.findByUserAndTask(userEntity, taskEntity);
        if (optional.isPresent()){
            throw new RepeatAssignException(Constant.TASK_ASSIGNED);
        }

        int compareValue = LocalDate.now().compareTo(taskEntity.getDueDate());
        if(compareValue > 0){
            throw new TaskOverdueException(Constant.TASK_OVERDUE);
        }

        TaskAssignmentEntity taskAssignmentEntity = TaskMapper
                .mapTaskAssignmentEntity(userEntity, taskEntity, taskDto.getComment(), userAuthenticated);
        taskAssignmentRepository.save(taskAssignmentEntity);

        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.TASK_ASSIGN_SUCCESS);
    }
}
