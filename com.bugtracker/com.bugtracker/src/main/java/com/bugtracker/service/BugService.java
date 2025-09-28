package com.bugtracker.service;

import com.bugtracker.dto.BugTableDTO;
import com.bugtracker.model.BugTableModel;
import java.util.List;

public interface BugService {

	BugTableDTO createBug(Long projectId, BugTableDTO bugDTO);

//    List<BugTableModel> getBugsByProject(Long projectId, String status, Long assigneeId, String priority);

//    BugTableModel getBugById(Long id);

    BugTableDTO updateBug(Long id, BugTableDTO bugDTO);

    BugTableDTO changeStatus(Long id, String status);

    BugTableDTO assignBug(Long id, Long assigneeId);

	List<BugTableDTO> getBugsByProject(Long projectId, String status, Long assigneeId, String priority);

	BugTableDTO getBugDTOById(Long id);

	

	
}
