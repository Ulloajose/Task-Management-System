package com.taskmanager.domain.service;

import com.sun.security.auth.UserPrincipal;
import com.taskmanager.domain.dto.task.ChangeStatusTaskDto;
import com.taskmanager.domain.dto.task.CreateTaskDto;
import com.taskmanager.domain.dto.task.TableTaskDto;
import com.taskmanager.domain.dto.task.UpdateTaskDto;
import com.taskmanager.domain.entity.TaskEntity;
import com.taskmanager.domain.entity.TaskStatusEntity;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.domain.exeption.NotFoundException;
import com.taskmanager.domain.exeption.TaskOverdueException;
import com.taskmanager.domain.model.DataTablesResponse;
import com.taskmanager.domain.model.DetailResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.model.ResultResponse;
import com.taskmanager.domain.repository.TaskRepository;
import com.taskmanager.util.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TaskService.class})
@ExtendWith(SpringExtension.class)
class TaskServiceTest {
    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @Test
    void testFindByIdActive() {
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
        Optional<TaskEntity> ofResult = Optional.of(taskEntity);
        when(taskRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult);
        TaskEntity actualFindByIdActiveResult = taskService.findByIdActive(1L);
        verify(taskRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        assertSame(taskEntity, actualFindByIdActiveResult);
    }

    @Test
    void testFindByIdActive2() {
        Optional<TaskEntity> emptyResult = Optional.empty();
        when(taskRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(NotFoundException.class, () -> taskService.findByIdActive(1L));
        verify(taskRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
    }

    @Test
    void testFindByIdActive3() {
        when(taskRepository.findByIdAndDeletedFalse(Mockito.<Long>any()))
                .thenThrow(new TaskOverdueException("An error occurred"));
        assertThrows(TaskOverdueException.class, () -> taskService.findByIdActive(1L));
        verify(taskRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
    }

    @Test
    void testCreate() {
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
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(userEntity);

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
        when(taskRepository.save(Mockito.<TaskEntity>any())).thenReturn(taskEntity);
        CreateTaskDto taskDto = new CreateTaskDto("Name", "The characteristics of someone or something", "2020-03-01",
                TaskStatus.PENDING);

        GenericResponse<Void> actualCreateResult = taskService.create(taskDto, new UserPrincipal("principal"));
        verify(userService).findByUsername(Mockito.<String>any());
        verify(taskRepository).save(Mockito.<TaskEntity>any());
        ResultResponse result = actualCreateResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        assertEquals("The changes have been saved successfully!", getResult.getDetail());
        assertNull(actualCreateResult.getData());
        assertEquals(1, details.size());
    }

    @Test
    void testCreateError() {
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
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(userEntity);
        when(taskRepository.save(Mockito.<TaskEntity>any())).thenThrow(new TaskOverdueException("An error occurred"));
        CreateTaskDto taskDto = new CreateTaskDto("Name", "The characteristics of someone or something", "2020-03-01",
                TaskStatus.PENDING);

        assertThrows(TaskOverdueException.class, () -> taskService.create(taskDto, new UserPrincipal("principal")));
        verify(userService).findByUsername(Mockito.<String>any());
        verify(taskRepository).save(Mockito.<TaskEntity>any());
    }

    @Test
    void testDelete() {
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
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(userEntity);

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
        Optional<TaskEntity> ofResult = Optional.of(taskEntity);

        TaskStatusEntity taskStatus2 = new TaskStatusEntity();
        taskStatus2.setCreatedBy(1L);
        taskStatus2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus2.setDeleted(true);
        taskStatus2.setId(1);
        taskStatus2.setLastModifiedBy(1L);
        taskStatus2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus2.setName(TaskStatus.PENDING);

        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setCreatedBy(1L);
        taskEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskEntity2.setDeleted(true);
        taskEntity2.setDescription("The characteristics of someone or something");
        taskEntity2.setDueDate(LocalDate.of(1970, 1, 1));
        taskEntity2.setId(1L);
        taskEntity2.setLastModifiedBy(1L);
        taskEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskEntity2.setName("Name");
        taskEntity2.setTaskStatus(taskStatus2);
        when(taskRepository.save(Mockito.<TaskEntity>any())).thenReturn(taskEntity2);
        when(taskRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult);
        GenericResponse<Void> actualDeleteResult = taskService.delete(1L, new UserPrincipal("principal"));
        verify(taskRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userService).findByUsername(Mockito.<String>any());
        verify(taskRepository).save(Mockito.<TaskEntity>any());
        ResultResponse result = actualDeleteResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        assertEquals("The task have been deleted successfully!", getResult.getDetail());
        assertNull(actualDeleteResult.getData());
        assertEquals(1, details.size());
    }

    @Test
    void testUpdate() {
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
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(userEntity);

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
        Optional<TaskEntity> ofResult = Optional.of(taskEntity);

        TaskStatusEntity taskStatus2 = new TaskStatusEntity();
        taskStatus2.setCreatedBy(1L);
        taskStatus2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus2.setDeleted(true);
        taskStatus2.setId(1);
        taskStatus2.setLastModifiedBy(1L);
        taskStatus2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskStatus2.setName(TaskStatus.PENDING);

        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setCreatedBy(1L);
        taskEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskEntity2.setDeleted(true);
        taskEntity2.setDescription("The characteristics of someone or something");
        taskEntity2.setDueDate(LocalDate.of(1970, 1, 1));
        taskEntity2.setId(1L);
        taskEntity2.setLastModifiedBy(1L);
        taskEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        taskEntity2.setName("Name");
        taskEntity2.setTaskStatus(taskStatus2);
        when(taskRepository.save(Mockito.<TaskEntity>any())).thenReturn(taskEntity2);
        when(taskRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult);
        UpdateTaskDto taskDto = new UpdateTaskDto("Name", "The characteristics of someone or something", "2020-03-01",
                TaskStatus.PENDING);

        GenericResponse<Void> actualUpdateResult = taskService.update(1L, taskDto, new UserPrincipal("principal"));
        verify(taskRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userService).findByUsername(Mockito.<String>any());
        verify(taskRepository).save(Mockito.<TaskEntity>any());
        ResultResponse result = actualUpdateResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        assertEquals("The task have been updated successfully!", getResult.getDetail());
        assertNull(actualUpdateResult.getData());
        assertEquals(1, details.size());
    }

    @Test
    void testFindAll() throws NotFoundException {
        when(taskRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        GenericResponse<DataTablesResponse<TableTaskDto>> actualFindAllResult = taskService.findAll(1, 3,
                new String[]{"Sort", "42"});
        verify(taskRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableTaskDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(0L, data.getTotalItems());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, details.size());
        assertTrue(data.getData().isEmpty());
    }

    @Test
    void testFindAllMoraData() throws NotFoundException {
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
        taskEntity.setName(",");
        taskEntity.setTaskStatus(taskStatus);

        ArrayList<TaskEntity> content = new ArrayList<>();
        content.add(taskEntity);
        PageImpl<TaskEntity> pageImpl = new PageImpl<>(content);
        when(taskRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(pageImpl);
        GenericResponse<DataTablesResponse<TableTaskDto>> actualFindAllResult = taskService.findAll(1, 3,
                new String[]{"Sort", "42"});
        verify(taskRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableTaskDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, data.getData().size());
        assertEquals(1, details.size());
        assertEquals(1L, data.getTotalItems());
    }

    @Test
    void testFindAllError() throws NotFoundException {
        when(taskRepository.findAllByDeletedFalse(Mockito.<Pageable>any()))
                .thenThrow(new TaskOverdueException("An error occurred"));
        assertThrows(TaskOverdueException.class, () -> taskService.findAll(1, 3, new String[]{"Sort", "42"}));
        verify(taskRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
    }

    @Test
    void testChangeStatus() {
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
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(userEntity);
        when(taskRepository.findByIdAndDeletedFalse(Mockito.<Long>any()))
                .thenThrow(new TaskOverdueException("An error occurred"));
        ChangeStatusTaskDto taskDto = new ChangeStatusTaskDto(TaskStatus.PENDING);
        assertThrows(TaskOverdueException.class,
                () -> taskService.changeStatus(1L, taskDto, new UserPrincipal("principal")));
        verify(taskRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userService).findByUsername(Mockito.<String>any());
    }
}
