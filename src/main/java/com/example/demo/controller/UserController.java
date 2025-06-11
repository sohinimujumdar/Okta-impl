package com.example.demo.controller;


import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔹 Create user
    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            System.out.println("Received user: " + user);
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // optionally rethrow or return a meaningful error response
        }
    }

    // 🔹 List all users
    @GetMapping
    public List<User> getUsers() {
        System.out.println("HI get");
        return userRepository.findAll();
    }
}
