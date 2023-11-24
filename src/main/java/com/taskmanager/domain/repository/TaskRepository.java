package com.taskmanager.domain.repository;

import com.taskmanager.domain.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByIdAndDeletedFalse(Long id);
    Page<TaskEntity> findAllByDeletedFalse(Pageable pageable);
}
