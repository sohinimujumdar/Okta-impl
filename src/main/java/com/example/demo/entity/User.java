package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @NotBlank
    private String id;
    private String username;

    private String password;

//    @Enumerated(EnumType.STRING)
//    private Role role;

//    public enum Role {
//        ADMIN,
//        USER
//    }
}



