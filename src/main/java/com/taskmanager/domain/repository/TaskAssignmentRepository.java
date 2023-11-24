package com.taskmanager.domain.repository;

import com.taskmanager.domain.entity.TaskAssignmentEntity;
import com.taskmanager.domain.entity.TaskEntity;
import com.taskmanager.domain.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskAssignmentRepository extends CrudRepository<TaskAssignmentEntity, Long> {

    Optional<TaskAssignmentEntity> findByUserAndTask(UserEntity userEntity, TaskEntity taskEntity);
}
