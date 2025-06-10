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
        System.out.println("HI");
        return userRepository.save(user);
    }

    // 🔹 List all users
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
