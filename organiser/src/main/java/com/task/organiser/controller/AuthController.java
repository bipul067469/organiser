package com.task.organiser.controller;

import com.task.organiser.entity.User;
import com.task.organiser.security.JWTUtil;
import com.task.organiser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User authenticatedUser = userService.login(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            return jwtUtil.generateToken(authenticatedUser.getUsername());
        }
        throw new RuntimeException("Invalid credentials");
    }
}
