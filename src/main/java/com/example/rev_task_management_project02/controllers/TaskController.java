package com.example.rev_task_management_project02.controllers;


import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task newTask) {
        Task createdTask = taskService.createTask(newTask);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        try {
            Task task = taskService.updateTask(id, updatedTask);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTaskById(id);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{taskId}/timestamps")
    public ResponseEntity<List<TimeStamp>> getTaskTimestamps(@PathVariable("taskId") long taskId) {
        List<TimeStamp> timeStamps = taskService.getTimeStampsForTask(taskId);
        return ResponseEntity.ok(timeStamps);
    }
}
