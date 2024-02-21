package com.project.service;

import com.project.dto.UserDTO;
import com.project.model.User;

import java.util.Optional;

public interface UserService extends Generic<UserDTO, UserDTO>{
    UserDTO findByEmail(String email);
}
