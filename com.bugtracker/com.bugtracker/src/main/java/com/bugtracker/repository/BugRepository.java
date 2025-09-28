package com.bugtracker.repository;

import com.bugtracker.model.BugPriority;
import com.bugtracker.model.BugStatus;
import com.bugtracker.model.BugTableModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<BugTableModel, Long> {

	List<BugTableModel> findByProject_ProjectIdAndStatus(Long projectId, BugStatus status);

	List<BugTableModel> findByProject_ProjectIdAndPriority(Long projectId, BugPriority priority);

	List<BugTableModel> findByProject_ProjectIdAndAssigneeId(Long projectId, Long assigneeId);

	List<BugTableModel> findByProject_ProjectIdAndAssigneeIdAndStatus(Long projectId, Long assigneeId, BugStatus status);

	List<BugTableModel> findByProject_ProjectId(Long projectId);

}
