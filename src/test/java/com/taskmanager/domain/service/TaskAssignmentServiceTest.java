package com.taskmanager.domain.service;

import com.sun.security.auth.UserPrincipal;
import com.taskmanager.domain.dto.task.AssignTaskDto;
import com.taskmanager.domain.entity.TaskAssignmentEntity;
import com.taskmanager.domain.entity.TaskEntity;
import com.taskmanager.domain.entity.TaskStatusEntity;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.domain.exeption.RepeatAssignException;
import com.taskmanager.domain.repository.TaskAssignmentRepository;
import com.taskmanager.util.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TaskAssignmentService.class})
@ExtendWith(SpringExtension.class)
class TaskAssignmentServiceTest {
    @MockBean
    private TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @Test
    void testAssignTask() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        when(userService.findByIdActive(Mockito.<Long>any())).thenReturn(userEntity);
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(userEntity2);

        TaskStatusEntity taskStatus = new TaskStatusEntity();
        taskStatus.setCreatedBy(1L);
        taskStatus.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus.setDeleted(true);
        taskStatus.setId(1);
        taskStatus.setLastModifiedBy(1L);
        taskStatus.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus.setName(TaskStatus.PENDING);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setCreatedBy(1L);
        taskEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskEntity.setDeleted(true);
        taskEntity.setDescription("The characteristics of someone or something");
        taskEntity.setDueDate(LocalDate.of(1970, 1, 1));
        taskEntity.setId(1L);
        taskEntity.setLastModifiedBy(1L);
        taskEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskEntity.setName("Name");
        taskEntity.setTaskStatus(taskStatus);
        when(taskService.findByIdActive(Mockito.<Long>any())).thenReturn(taskEntity);

        TaskStatusEntity taskStatus2 = new TaskStatusEntity();
        taskStatus2.setCreatedBy(1L);
        taskStatus2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus2.setDeleted(true);
        taskStatus2.setId(1);
        taskStatus2.setLastModifiedBy(1L);
        taskStatus2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus2.setName(TaskStatus.PENDING);

        TaskEntity task = new TaskEntity();
        task.setCreatedBy(1L);
        task.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        task.setDeleted(true);
        task.setDescription("The characteristics of someone or something");
        task.setDueDate(LocalDate.of(1970, 1, 1));
        task.setId(1L);
        task.setLastModifiedBy(1L);
        task.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        task.setName("Name");
        task.setTaskStatus(taskStatus2);

        UserEntity user = new UserEntity();
        user.setCreatedBy(1L);
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setDeleted(true);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setLastModifiedBy(1L);
        user.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");

        TaskAssignmentEntity taskAssignmentEntity = new TaskAssignmentEntity();
        taskAssignmentEntity.setComment("Comment");
        taskAssignmentEntity.setCreatedBy(1L);
        taskAssignmentEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskAssignmentEntity.setDeleted(true);
        taskAssignmentEntity.setId(1L);
        taskAssignmentEntity.setLastModifiedBy(1L);
        taskAssignmentEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskAssignmentEntity.setTask(task);
        taskAssignmentEntity.setUser(user);
        Optional<TaskAssignmentEntity> ofResult = Optional.of(taskAssignmentEntity);
        when(taskAssignmentRepository.findByUserAndTask(Mockito.<UserEntity>any(), Mockito.<TaskEntity>any()))
                .thenReturn(ofResult);
        AssignTaskDto taskDto = new AssignTaskDto();
        assertThrows(RepeatAssignException.class,
                () -> taskAssignmentService.assignTask(1L, taskDto, new UserPrincipal("principal")));
        verify(taskAssignmentRepository).findByUserAndTask(Mockito.<UserEntity>any(), Mockito.<TaskEntity>any());
        verify(taskService).findByIdActive(Mockito.<Long>any());
        verify(userService).findByIdActive(Mockito.<Long>any());
        verify(userService).findByUsername(Mockito.<String>any());
    }
}
