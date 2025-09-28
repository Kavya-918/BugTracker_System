package com.bugtracker.service;


import java.util.List;

import com.bugtracker.dto.ProjectTableDTO;

public interface ProjectService {

    ProjectTableDTO createProject(ProjectTableDTO projectDTO);

    List<ProjectTableDTO> getAllProjects();

    ProjectTableDTO getProjectById(Long projectId);

    ProjectTableDTO updateProject(Long projectId, ProjectTableDTO projectDTO);

    void deleteProject(Long projectId);
}
