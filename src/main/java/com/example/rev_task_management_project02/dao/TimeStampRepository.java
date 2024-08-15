package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.TimeStamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeStampRepository extends JpaRepository<TimeStamp, Long> {
}