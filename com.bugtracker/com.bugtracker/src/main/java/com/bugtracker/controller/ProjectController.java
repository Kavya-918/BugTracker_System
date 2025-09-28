package com.bugtracker.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bugtracker.dto.ProjectTableDTO;
import com.bugtracker.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    //http://localhost:9090/projects/createproject
    @PostMapping("/createproject")
    public ResponseEntity<ProjectTableDTO> createProject(@RequestBody ProjectTableDTO dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    //http://localhost:9090/projects/getallproject
    @GetMapping("/getallproject")
    public ResponseEntity<List<ProjectTableDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    
    //http://localhost:9090/projects/getprojectbyid/1
    @GetMapping("/getprojectbyid/{id}")
    public ResponseEntity<ProjectTableDTO> getProjectById(@PathVariable Long id) {
        ProjectTableDTO project = projectService.getProjectById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return ResponseEntity.notFound().build();
    }

    
    //updating project based on project id not candidate id
    //http://localhost:9090/projects/updateprojectbyid/3
    @PutMapping(value = "/updateprojectbyid/{id}", consumes = {"application/json", "text/plain"})
    public ResponseEntity<ProjectTableDTO> updateProject(@PathVariable Long id, @RequestBody ProjectTableDTO dto) {
        System.out.println("➡️ Inside Controller: updateprojectbyid, id=" + id);
        System.out.println("➡️ Incoming DTO: " + dto);

        ProjectTableDTO updated = projectService.updateProject(id, dto);

        if (updated != null) {
            System.out.println("✅ Controller returning 200 with updated project: " + updated);
            return ResponseEntity.ok(updated);
        }

        System.out.println("❌ Controller returning 404: project not found for id=" + id);
        return ResponseEntity.notFound().build();
    }


    //http://localhost:9090/projects/deleteprojectbyid/3
    @DeleteMapping("/deleteprojectbyid/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
