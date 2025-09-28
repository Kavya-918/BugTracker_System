package com.bugtracker.serviceimp;



import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugtracker.dto.ProjectTableDTO;
import com.bugtracker.model.ProjectTableModel;
import com.bugtracker.model.RegisterModel;
import com.bugtracker.repository.LoginRepo;
import com.bugtracker.repository.ProjectTableRepository;
import com.bugtracker.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectTableRepository projectTableRepository;

    @Autowired
    private LoginRepo registerRepository;

    @Override
    public ProjectTableDTO createProject(ProjectTableDTO dto) {
        ProjectTableModel model = new ProjectTableModel();

        // Fetch candidate entity from user_master
        RegisterModel candidate = registerRepository.findById(dto.getCandidateID())
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + dto.getCandidateID()));

        model.setCandidateID(candidate);
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setDeadline(dto.getDeadline());
        model.setAssignDate(dto.getAssignDate());
        model.setRemarks(dto.getRemarks());
        model.setStatus(dto.getStatus());
        model.setPriority(dto.getPriority());

        // Use provided createdAt/updatedAt if present, else default to now
        model.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        model.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());

        ProjectTableModel saved = projectTableRepository.save(model);

        // Set saved values back to DTO
        dto.setProjectId(saved.getProjectId());
        dto.setCreatedAt(saved.getCreatedAt());
        dto.setUpdatedAt(saved.getUpdatedAt());

        return dto;
    }

    
    
    
    
    
    

    @Override
    public List<ProjectTableDTO> getAllProjects() {
        return projectTableRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ProjectTableDTO getProjectById(Long projectId) {
        return projectTableRepository.findById(projectId).map(this::convertToDTO).orElse(null);
    }

    
    
    
    
    
    @Override
    public ProjectTableDTO updateProject(Long projectId, ProjectTableDTO dto) {
        System.out.println("➡️ Inside Service: updateProject, projectId=" + projectId);

        return projectTableRepository.findById(projectId).map(project -> {
            System.out.println("✔️ Project found in DB: " + project);

            RegisterModel candidate = registerRepository.findById(dto.getCandidateID())
                    .orElseThrow(() -> {
                        System.out.println("❌ Candidate not found, id=" + dto.getCandidateID());
                        return new RuntimeException("Candidate not found with id: " + dto.getCandidateID());
                    });

            System.out.println("✔️ Candidate found: " + candidate);

            project.setCandidateID(candidate);
            project.setName(dto.getName());
            project.setDescription(dto.getDescription());
            project.setDeadline(dto.getDeadline());
            project.setAssignDate(dto.getAssignDate());
            project.setRemarks(dto.getRemarks());
            project.setStatus(dto.getStatus());
            project.setPriority(dto.getPriority());
            project.setUpdatedAt(LocalDateTime.now());

            ProjectTableDTO savedDTO = convertToDTO(projectTableRepository.save(project));
            System.out.println("✅ Project updated and saved: " + savedDTO);
            return savedDTO;
        }).orElseGet(() -> {
            System.out.println("❌ No project found with id=" + projectId);
            return null;
        });
    }

    @Override
    public void deleteProject(Long projectId) {
        projectTableRepository.deleteById(projectId);
    }

    private ProjectTableDTO convertToDTO(ProjectTableModel model) {
        ProjectTableDTO dto = new ProjectTableDTO();
        dto.setProjectId(model.getProjectId());
        dto.setCandidateID(model.getCandidateID().getCandidateId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setDeadline(model.getDeadline());
        dto.setAssignDate(model.getAssignDate());
        dto.setRemarks(model.getRemarks());
        dto.setStatus(model.getStatus());
        dto.setPriority(model.getPriority());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        return dto;
    }
}
