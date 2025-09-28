package com.bugtracker.serviceimp;

import com.bugtracker.dto.RegisterDTO;
import com.bugtracker.model.RegisterModel;
import com.bugtracker.repository.LoginRepo;
import com.bugtracker.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepo loginRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String register(RegisterDTO dto) {
        RegisterModel user = new RegisterModel();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setCompanyId(dto.getCompanyId());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setRole(dto.getRole());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setJoiningDate(dto.getJoiningDate());

        loginRepo.save(user);
        return "User registered successfully!";
    }

    @Override
    public String login(String email, String password) {
        Optional<RegisterModel> userOpt = loginRepo.findByEmail(email);
        if (userOpt.isEmpty()) return "User not found!";

        RegisterModel user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return "Login Successful!";
        } else {
            return "Invalid credentials!";
        }
    }

    @Override
    public String resetPassword(String email, String newPassword) {
        Optional<RegisterModel> userOpt = loginRepo.findByEmail(email);
        if (userOpt.isEmpty()) return "User not found!";

        RegisterModel user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        loginRepo.save(user);

        return "Password reset successful!";
    }
}
