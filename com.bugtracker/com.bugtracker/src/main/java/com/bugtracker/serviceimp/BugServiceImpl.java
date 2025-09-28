package com.bugtracker.serviceimp;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugtracker.dto.BugTableDTO;
import com.bugtracker.model.BugPriority;
import com.bugtracker.model.BugStatus;
import com.bugtracker.model.BugTableModel;
import com.bugtracker.model.ProjectTableModel;
import com.bugtracker.model.RegisterModel;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.LoginRepo;
import com.bugtracker.repository.ProjectTableRepository;
import com.bugtracker.service.BugService;

@Service
public class BugServiceImpl implements BugService {

    @Autowired
    private BugRepository bugRepository;
    
    @Autowired
    private EmailServiceImpl emailService;
    
    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private ProjectTableRepository projectRepository;
    
    
    
    @Override
    public BugTableDTO createBug(Long projectId, BugTableDTO bugDTO) {
        ProjectTableModel project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        BugTableModel bug = new BugTableModel();
        bug.setProject(project);
        bug.setBugDescription(bugDTO.getBugDescription());
        bug.setReporterId(bugDTO.getReporterId());
        bug.setAssigneeId(bugDTO.getAssigneeId());
        bug.setStatus(BugStatus.OPEN);
        bug.setPriority(bugDTO.getPriority() != null ? bugDTO.getPriority() : BugPriority.MEDIUM);
        bug.setRemarks(bugDTO.getRemarks());
        bug.setCreatedAt(LocalDateTime.now());
        bug.setUpdatedAt(LocalDateTime.now());
        bug.setBugReportedName(bugDTO.getBugReportedName());
        bug.setBugSolvedName(bugDTO.getBugSolvedName());

        // ✅ Set reporter email permanently in bug_table
        bug.setReporterEmail(bugDTO.getReporterEmail());  // <- pass reporter email in DTO

        BugTableModel savedBug = bugRepository.save(bug);

        // ✅ Fetch assignee if exists
        RegisterModel assignee = (bugDTO.getAssigneeId() != null)
                ? loginRepo.findById(bugDTO.getAssigneeId()).orElse(null)
                : null;

        // ✅ Print emails for debugging
        System.out.println("Reporter Email (from bug_table): " + savedBug.getReporterEmail());
        System.out.println("Assignee Email (from user_master): " + (assignee != null ? assignee.getEmail() : "Unassigned"));

        // ✅ Send email using the reporter_email stored in bug_table
        emailService.sendIssueCreatedEmail(savedBug, savedBug.getReporterEmail(),
                                           assignee != null ? assignee.getEmail() : null);

        return mapToDTO(savedBug);
    }



    private BugTableDTO mapToDTO(BugTableModel bug) {
        BugTableDTO dto = new BugTableDTO();
        dto.setProjectId(bug.getProject().getProjectId());
        dto.setProjectName(bug.getProject().getName());
        dto.setBugDescription(bug.getBugDescription());
        dto.setReporterId(bug.getReporterId());
        dto.setAssigneeId(bug.getAssigneeId());
        dto.setStatus(bug.getStatus());
        dto.setPriority(bug.getPriority());
        dto.setRemarks(bug.getRemarks());
        dto.setCreatedAt(bug.getCreatedAt());
        dto.setUpdatedAt(bug.getUpdatedAt());
        dto.setBugReportedName(bug.getBugReportedName());
        dto.setBugSolvedName(bug.getBugSolvedName());
        return dto;
    }
   //-------------------------------------------------------------------

    @Override
    public List<BugTableDTO> getBugsByProject(Long projectId, String status, Long assigneeId, String priority) {
        System.out.println("Fetching bugs for projectId: " + projectId);

        BugStatus bugStatus = null;
        BugPriority bugPriority = null;

        if (status != null) {
            try {
                bugStatus = BugStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid BugStatus value: " + status);
            }
        }

        if (priority != null) {
            try {
                bugPriority = BugPriority.valueOf(priority.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid BugPriority value: " + priority);
            }
        }

        List<BugTableModel> bugs;

        if (bugStatus != null && assigneeId != null) {
            bugs = bugRepository.findByProject_ProjectIdAndAssigneeIdAndStatus(projectId, assigneeId, bugStatus);
        } else if (bugStatus != null) {
            bugs = bugRepository.findByProject_ProjectIdAndStatus(projectId, bugStatus);
        } else if (assigneeId != null) {
            bugs = bugRepository.findByProject_ProjectIdAndAssigneeId(projectId, assigneeId);
        } else if (bugPriority != null) {
            bugs = bugRepository.findByProject_ProjectIdAndPriority(projectId, bugPriority);
        } else {
            bugs = bugRepository.findByProject_ProjectId(projectId);
        }

        // ✅ map entity -> DTO
        return bugs.stream().map(this::mapToDTO).toList();
    }

    private BugTableDTO mapToDTO1(BugTableModel bug) {
        BugTableDTO dto = new BugTableDTO();

        // Project details
//        dto.setProjectId(bug.getProject() != null ? bug.getProject().getId() : null);
        dto.setProjectName(bug.getProject() != null ? bug.getProject().getName() : null);

        // Bug details
        dto.setBugDescription(bug.getBugDescription());
        dto.setReporterId(bug.getReporterId());
        dto.setAssigneeId(bug.getAssigneeId());
        dto.setStatus(bug.getStatus());
        dto.setPriority(bug.getPriority());
        dto.setRemarks(bug.getRemarks());
        dto.setCreatedAt(bug.getCreatedAt());
        dto.setUpdatedAt(bug.getUpdatedAt());

        // Reporter / Solver names (direct fields in entity)
        dto.setBugReportedName(bug.getBugReportedName());
        dto.setBugSolvedName(bug.getBugSolvedName());

        return dto;
    }

//-----------------------------------------------------------------------

    public BugTableDTO getBugDTOById(Long id) {
        BugTableModel bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
        return mapToDTO(bug); // manually map only required fields
    }

    @Override
    public BugTableDTO updateBug(Long id, BugTableDTO bugDTO) {
        System.out.println("Updating bug with id: " + id);
        BugTableModel bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        if (bugDTO.getBugDescription() != null) bug.setBugDescription(bugDTO.getBugDescription());
        if (bugDTO.getPriority() != null) bug.setPriority(bugDTO.getPriority());
        if (bugDTO.getRemarks() != null) bug.setRemarks(bugDTO.getRemarks());
        bug.setUpdatedAt(LocalDateTime.now());

        BugTableModel savedBug = bugRepository.save(bug);
        return mapToDTO(savedBug); // map only required fields to DTO
    }


    @Override
    public BugTableDTO changeStatus(Long id, String status) {
        System.out.println("Changing status of bug with id: " + id + " to " + status);
        BugTableModel bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        bug.setStatus(BugStatus.valueOf(status));
        bug.setUpdatedAt(LocalDateTime.now());

        BugTableModel saved = bugRepository.save(bug);
        return mapToDTO(saved); // map only safe fields
    }


    @Override
    public BugTableDTO assignBug(Long id, Long assigneeId) {
        System.out.println("Assigning bug with id: " + id + " to assigneeId: " + assigneeId);
        BugTableModel bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        bug.setAssigneeId(assigneeId);
        bug.setUpdatedAt(LocalDateTime.now());

        BugTableModel saved = bugRepository.save(bug);
        return mapToDTO(saved);
    }

}
