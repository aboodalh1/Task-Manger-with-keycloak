package com.example.task_manger.tasks.repository;

import com.example.task_manger.tasks.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByOwnerId(Pageable pageable,String ownerId);

    void deleteById(Long id);
}