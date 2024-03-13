package com.project.service;

import com.project.dto.request.UserDTO;
import com.project.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserService extends Generic<UserDTO, UserDTO>{
    UserDTO findByEmail(String email);
}
