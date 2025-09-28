package com.bugtracker.controller;

import com.bugtracker.dto.RegisterDTO;
import com.bugtracker.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    
    //http://localhost:9090/auth/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dto) {
    	System.out.println("hitting register method kavyaa");
        return ResponseEntity.ok(loginService.register(dto));
    }

    //http://localhost:9090/auth/login?email=kavyadeveloper918@gmail.com&password=Harish@918
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(loginService.login(email, password));
    }

    //http://localhost:9090/auth/reset-password?email=kavyadeveloper918@gmail.com&newPassword=Harish_918
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        return ResponseEntity.ok(loginService.resetPassword(email, newPassword));
    }
}
