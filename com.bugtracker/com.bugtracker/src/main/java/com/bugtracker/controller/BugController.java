package com.bugtracker.controller;

import com.bugtracker.dto.BugTableDTO;
import com.bugtracker.model.BugTableModel;
import com.bugtracker.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bug")
public class BugController {

    @Autowired
    private BugService bugService;

    //create bug api
    //http://localhost:9090/api/bug/projects/1/issues
    @PostMapping("/projects/{projectId}/issues")
    public ResponseEntity<BugTableDTO> createBug(@PathVariable Long projectId, @RequestBody BugTableDTO bugDTO) {
        System.out.println("API Call: Create Bug");
        BugTableDTO bugResponse = bugService.createBug(projectId, bugDTO);
        return ResponseEntity.ok(bugResponse);
    }

    
    /*
     * GET http://localhost:9090/api/bug/1/issues
       GET http://localhost:9090/api/bug/1/issues?status=OPEN
       GET http://localhost:9090/api/bug/1/issues?assigneeId=3
       GET http://localhost:9090/api/bug/1/issues?priority=HIGH

     */

    @GetMapping("/{projectId}/issues")
    public ResponseEntity<List<BugTableDTO>> getBugsByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String priority) {

        System.out.println("API Call: Get Bugs By Project");
        List<BugTableDTO> bugs = bugService.getBugsByProject(projectId, status, assigneeId, priority);
        return ResponseEntity.ok(bugs);
    }
    
    
   //http://localhost:9090/api/bug/issues/2
    @GetMapping("/issues/{id}")
    public ResponseEntity<BugTableDTO> getBugById(@PathVariable Long id) {
        BugTableDTO dto = bugService.getBugDTOById(id);
        return ResponseEntity.ok(dto);
    }


    //http://localhost:9090/api/bug/issues/1
    @PutMapping("/issues/{id}")
    public ResponseEntity<BugTableDTO> updateBug(@PathVariable Long id, @RequestBody BugTableDTO bugDTO) {
        System.out.println("API Call: Update Bug");
        BugTableDTO updatedBug = bugService.updateBug(id, bugDTO);
        return ResponseEntity.ok(updatedBug);
    }

    
    
    //http://localhost:9090/api/bug/issues/1/status?status=IN_PROGRESS
    @PatchMapping("/issues/{id}/status")
    public ResponseEntity<BugTableDTO> changeStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        System.out.println("API Call: Change Status");
        BugTableDTO bug = bugService.changeStatus(id, status);
        return ResponseEntity.ok(bug);
    }

    //http://localhost:9090/api/bug/issues/2/assign?assigneeId=3
    @PatchMapping("/issues/{id}/assign")
    public ResponseEntity<BugTableDTO> assignBug(
            @PathVariable Long id,
            @RequestParam Long assigneeId) {
        System.out.println("API Call: Assign Bug");
        BugTableDTO bug = bugService.assignBug(id, assigneeId);
        return ResponseEntity.ok(bug);
    }

}
