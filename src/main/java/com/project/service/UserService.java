package com.project.service;

import com.project.model.User;

import java.util.Optional;

public interface UserService extends Generic<User, Integer>{
    Optional<User> findByEmail(String email);
}
