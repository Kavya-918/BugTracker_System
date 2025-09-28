package com.bugtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bugtracker.model.AttachmentModel;

public interface AttachmentRepo extends JpaRepository<AttachmentModel, Long> {
    // Optional: you can add custom methods if needed
	List<AttachmentModel> findByCommentId(Long commentId);
}
