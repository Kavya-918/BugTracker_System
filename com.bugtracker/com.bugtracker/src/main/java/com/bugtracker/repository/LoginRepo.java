package com.bugtracker.repository;

import com.bugtracker.model.RegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoginRepo extends JpaRepository<RegisterModel, Long> {
    Optional<RegisterModel> findByEmail(String email);
}
