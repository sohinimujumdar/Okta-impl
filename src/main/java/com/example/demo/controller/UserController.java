package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //Create user (Only ADMIN can create)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Get all users (Accessible by any authenticated user)
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Delete user by ID (Only ADMIN can delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return "User with ID " + id + " deleted successfully.";
                })
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));
    }
}
