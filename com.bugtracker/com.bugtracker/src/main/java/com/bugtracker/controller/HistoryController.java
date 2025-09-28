package com.bugtracker.controller;

import com.bugtracker.dto.*;
import com.bugtracker.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // http://localhost:9090/api/history/comments
    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO dto) {
        return ResponseEntity.ok(historyService.addComment(dto));
    }

    //http://localhost:9090/api/history/comments/1
    @GetMapping("/comments/{issueId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long issueId) {
        return ResponseEntity.ok(historyService.getCommentsByIssue(issueId));
    }

    // Attachment APIs
    //http://localhost:9090/api/history/attachments
    @PostMapping("/attachments")
    public ResponseEntity<AttachmentDTO> addAttachment(@RequestBody AttachmentDTO dto) {
        return ResponseEntity.ok(historyService.addAttachment(dto));
    }

    //http://localhost:9090/api/history/attachments/1
    @GetMapping("/attachments/{commentId}")
    public ResponseEntity<List<AttachmentDTO>> getAttachments(@PathVariable Long commentId) {
    	System.out.println("inside getAttachments");
        return ResponseEntity.ok(historyService.getAttachmentsByComment(commentId));
    }

    // History APIs
    //http://localhost:9090/api/history/logs
    @PostMapping("/logs")
    public ResponseEntity<IssueHistoryDTO> logHistory(@RequestBody IssueHistoryDTO dto) {
        return ResponseEntity.ok(historyService.logHistory(dto));
    }

    
    //http://localhost:9090/api/history/issue/1
    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<IssueHistoryDTO>> getHistoryByIssue(@PathVariable Long issueId) {
    	System.out.println("inside getHistoryByIssue");
        return ResponseEntity.ok(historyService.getHistoryByIssue(issueId));
    }
}
