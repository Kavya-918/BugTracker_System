package com.bugtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bugtracker.model.CommentModel;

public interface CommentRepo extends JpaRepository<CommentModel, Long> {
    // Optional: find comments by issue or project
}
