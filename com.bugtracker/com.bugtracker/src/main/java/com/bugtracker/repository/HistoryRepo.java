package com.bugtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bugtracker.model.CommentModel;


@Repository
public interface HistoryRepo extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findByIssue_Id(Long issueId); // example if you need to fetch by issue
}
