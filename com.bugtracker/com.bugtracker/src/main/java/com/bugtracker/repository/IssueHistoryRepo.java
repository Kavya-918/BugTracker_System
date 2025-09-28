//package com.bugtracker.repository;
//
//import com.bugtracker.model.IssueHistoryModel;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public interface IssueHistoryRepo extends JpaRepository<IssueHistoryModel, Long> {
//    List<IssueHistoryModel> findByComment_Id(Long commentId);
//}



package com.bugtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bugtracker.model.IssueHistoryModel;

public interface IssueHistoryRepo extends JpaRepository<IssueHistoryModel, Long> {
    List<IssueHistoryModel> findByIssueId(Long issueId);
}
