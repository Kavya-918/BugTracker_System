package com.bugtracker.service;

import com.bugtracker.dto.RegisterDTO;

public interface LoginService {
    String register(RegisterDTO dto);
    String login(String email, String password);
    String resetPassword(String email, String newPassword);
}
