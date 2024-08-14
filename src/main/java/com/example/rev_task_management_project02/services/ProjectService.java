package com.example.rev_task_management_project02.services;


import com.example.rev_task_management_project02.dao.ProjectRepository;
import com.example.rev_task_management_project02.exceptions.ProjectNotFoundException;
import com.example.rev_task_management_project02.models.Project;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EntityUpdater entityUpdater;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EntityUpdater entityUpdater) {
        this.projectRepository = projectRepository;
        this.entityUpdater = entityUpdater;
    }

    public Project getProjectById(Long id) throws ProjectNotFoundException {
        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project with ID " + id + " not found"));
    }

    public Project createProject(Project newProject) {
        return projectRepository.save(newProject);
    }

    public Project updateProject(Long id, Project updatedProject) throws ProjectNotFoundException {
        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project with ID " + id + " not found"));

        Project updateProject = entityUpdater.updateFields(existingProject, updatedProject);

        return projectRepository.save(updateProject);
    }

    public Project deleteProjectById(Long id) throws ProjectNotFoundException {
        if (projectRepository.existsById(id)) {
            Project project = projectRepository.findById(id).get();
            projectRepository.deleteById(id);
            return project;
        } else {
            throw new ProjectNotFoundException("Project not found with id " + id);
        }
    }



    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
