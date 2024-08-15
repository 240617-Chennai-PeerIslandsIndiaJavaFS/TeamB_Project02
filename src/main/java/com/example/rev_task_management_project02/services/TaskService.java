package com.example.rev_task_management_project02.services;

import com.example.rev_task_management_project02.dao.TaskRepository;
import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EntityUpdater entityUpdater;

    @Autowired
    public TaskService(TaskRepository taskRepository, EntityUpdater entityUpdater) {
        this.taskRepository = taskRepository;
        this.entityUpdater = entityUpdater;
    }

    public Task getTaskById(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    public Task createTask(Task newTask) {
        return taskRepository.save(newTask);
    }

    public Task updateTask(Long id, Task updatedTask) throws TaskNotFoundException {
        Task existingTask = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with ID " + id + " not found"));

        Task updatedTaskEntity = entityUpdater.updateFields(existingTask, updatedTask);

        return taskRepository.save(updatedTaskEntity);
    }

    public Task deleteTaskById(Long id) throws TaskNotFoundException {
        if (taskRepository.existsById(id)) {
            Task task = taskRepository.findById(id).get();
            taskRepository.deleteById(id);
            return task;
        } else {
            throw new TaskNotFoundException("Task not found with ID " + id);
        }
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
