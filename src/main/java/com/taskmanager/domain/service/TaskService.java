package com.taskmanager.domain.service;

import com.taskmanager.domain.dto.task.ChangeStatusTaskDto;
import com.taskmanager.domain.dto.task.CreateTaskDto;
import com.taskmanager.domain.dto.task.TableTaskDto;
import com.taskmanager.domain.dto.task.UpdateTaskDto;
import com.taskmanager.domain.entity.TaskEntity;
import com.taskmanager.domain.entity.TaskStatusEntity;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.domain.exeption.NotFoundException;
import com.taskmanager.domain.exeption.TaskOverdueException;
import com.taskmanager.domain.mapper.GenericResponseMapper;
import com.taskmanager.domain.mapper.TaskMapper;
import com.taskmanager.domain.model.DataTablesResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.repository.TaskRepository;
import com.taskmanager.util.Constant;
import com.taskmanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;

    public TaskEntity findByIdActive(Long id){
        return taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(Constant.TASK_NOT_FOUND));
    }

    public GenericResponse<Void> create(CreateTaskDto taskDto, Principal principal){
        UserEntity userAuthenticated = userService.findByUsername(principal.getName());
        taskRepository.save(TaskMapper.mapTaskEntity(taskDto, userAuthenticated));
        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.CHANGES_SUCCESS);
    }

    public GenericResponse<Void> delete(Long id, Principal principal){
        UserEntity userAuthenticated = userService.findByUsername(principal.getName());
        TaskEntity taskEntity = findByIdActive(id);
        taskEntity.setDeleted(true);
        taskEntity.setLastModifiedBy(userAuthenticated.getId());
        taskEntity.setLastModifiedDate(LocalDateTime.now());
        taskRepository.save(taskEntity);
        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.TASK_DELETED_SUCCESS);
    }

    public GenericResponse<Void> update(Long id, UpdateTaskDto taskDto, Principal principal){
        UserEntity userAuthenticated = userService.findByUsername(principal.getName());
        taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
        TaskEntity taskEntity = TaskMapper.mapTaskEntity(taskDto, userAuthenticated);
        taskEntity.setId(id);
        taskRepository.save(taskEntity);
        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.TASK_UPDATE_SUCCESS);
    }

    public GenericResponse<DataTablesResponse<TableTaskDto>> findAll(
            int pageNumber, int pageSize, String[] sort) throws NotFoundException {

        List<Sort.Order> orders = PaginationUtil.setSort(sort);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orders));
        Page<TaskEntity> taskEntityPage = taskRepository.findAllByDeletedFalse(pageable);
        List<TableTaskDto> bList = taskEntityPage
                .getContent()
                .stream()
                .map(TaskMapper::mapTableTaskDto)
                .toList();

        DataTablesResponse<TableTaskDto> tablesResponse = new DataTablesResponse<>();
        tablesResponse.setData(bList);
        tablesResponse.setCurrentPage(taskEntityPage.getNumber());
        tablesResponse.setTotalItems(taskEntityPage.getTotalElements());
        tablesResponse.setTotalPages(taskEntityPage.getTotalPages());
        return GenericResponseMapper.buildGenericResponse(tablesResponse, HttpStatus.OK, HttpStatus.OK.toString());
    }

    public GenericResponse<Void> changeStatus(Long id, ChangeStatusTaskDto taskDto, Principal principal){
        UserEntity userAuthenticated = userService.findByUsername(principal.getName());
        TaskEntity taskEntity = findByIdActive(id);

        int compareValue = LocalDate.now().compareTo(taskEntity.getDueDate());
        if(compareValue > 0){
            throw new TaskOverdueException(Constant.TASK_OVERDUE);
        }

        taskEntity.setTaskStatus(TaskStatusEntity.builder().id(taskDto.getTaskStatus().getId()).build());
        taskEntity.setLastModifiedDate(LocalDateTime.now());
        taskEntity.setLastModifiedBy(userAuthenticated.getId());
        taskRepository.save(taskEntity);
        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.TASK_UPDATE_SUCCESS);
    }
}
