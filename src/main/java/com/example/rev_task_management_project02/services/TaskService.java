package com.example.rev_task_management_project02.services;

import com.example.rev_task_management_project02.dao.TaskRepository;
import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EntityUpdater entityUpdater;
    private final TimeStampService timeStampService;



    @Autowired
    public TaskService(TaskRepository taskRepository, EntityUpdater entityUpdater,TimeStampService timeStampService) {
        this.taskRepository = taskRepository;
        this.entityUpdater = entityUpdater;
        this.timeStampService =  timeStampService;
    }

    public Task getTaskById(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    public Task createTask(Task newTask) {
        newTask.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newTask.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Task savedTask = taskRepository.save(newTask);

        if (savedTask.getMilestone() != null) {
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setTask(savedTask);
            timeStamp.setMilestone(savedTask.getMilestone());
            timeStamp.setTimeStamp(new Timestamp(System.currentTimeMillis()));

            timeStampService.createTimeStamp(timeStamp);
        }

        return savedTask;
    }

    public Task updateTask(Long id, Task updatedTask) throws TaskNotFoundException {
        Task existingTask = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with ID " + id + " not found"));

        Task updatedTaskEntity = entityUpdater.updateFields(existingTask, updatedTask);
        updatedTaskEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Task savedTask = taskRepository.save(updatedTaskEntity);

        if (savedTask.getMilestone() != null) {
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setTask(savedTask);
            timeStamp.setMilestone(savedTask.getMilestone());
            timeStamp.setTimeStamp(new Timestamp(System.currentTimeMillis()));

            timeStampService.createTimeStamp(timeStamp);
        }

        return savedTask;
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
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectProjectId(projectId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
