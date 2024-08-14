package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
