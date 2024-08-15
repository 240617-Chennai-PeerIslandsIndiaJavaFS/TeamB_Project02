package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
