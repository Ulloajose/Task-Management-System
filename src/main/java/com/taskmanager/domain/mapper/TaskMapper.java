package com.taskmanager.domain.mapper;


import com.taskmanager.domain.dto.task.CreateTaskDto;
import com.taskmanager.domain.dto.task.TableTaskDto;
import com.taskmanager.domain.dto.task.UpdateTaskDto;
import com.taskmanager.domain.entity.TaskAssignmentEntity;
import com.taskmanager.domain.entity.TaskEntity;
import com.taskmanager.domain.entity.TaskStatusEntity;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.util.DateUtil;
import com.taskmanager.util.TaskStatus;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Objects;

@UtilityClass
public class TaskMapper {

    public TaskEntity mapTaskEntity(CreateTaskDto taskDto, UserEntity userAuthenticated){
        if(Objects.isNull(taskDto.getTaskStatus())){
            taskDto.setTaskStatus(TaskStatus.PENDING);
        }
        return TaskEntity.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .dueDate(DateUtil.convertToLocalDate(taskDto.getDueDate()))
                .taskStatus(TaskStatusEntity.builder().id(taskDto.getTaskStatus().getId()).build())
                .createdDate(LocalDateTime.now())
                .createdBy(userAuthenticated.getId())
                .build();
    }

    public TaskEntity mapTaskEntity(UpdateTaskDto taskDto, UserEntity userAuthenticated){
        if(Objects.isNull(taskDto.getTaskStatus())){
            taskDto.setTaskStatus(TaskStatus.PENDING);
        }
        return TaskEntity.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .dueDate(DateUtil.convertToLocalDate(taskDto.getDueDate()))
                .taskStatus(TaskStatusEntity.builder().id(taskDto.getTaskStatus().getId()).build())
                .lastModifiedBy(userAuthenticated.getId())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    public TableTaskDto mapTableTaskDto(TaskEntity taskEntity){
        String status = "";
        if (Objects.nonNull(taskEntity.getTaskStatus())){
            status = taskEntity.getTaskStatus().getName().name();
        }
        return TableTaskDto.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .description(taskEntity.getDescription())
                .dueDate(DateUtil.convertLocalDateToString(taskEntity.getDueDate()))
                .createDate(DateUtil.convertLocalDateTimeToString(taskEntity.getCreatedDate()))
                .lastModifiedDate(DateUtil.convertLocalDateTimeToString(taskEntity.getLastModifiedDate()))
                .taskStatus(status)
                .build();
    }

    public TaskAssignmentEntity mapTaskAssignmentEntity(UserEntity userEntity, TaskEntity taskEntity, String comment, UserEntity userAuthenticated){
        return TaskAssignmentEntity.builder()
                .task(taskEntity)
                .user(userEntity)
                .comment(comment)
                .createdDate(LocalDateTime.now())
                .createdBy(userAuthenticated.getId())
                .build();
    }
}
