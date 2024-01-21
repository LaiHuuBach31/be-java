package com.project.controller;

import com.project.dto.CategoryDTO;
import com.project.dto.UserDTO;
import com.project.model.Category;
import com.project.model.Result;
import com.project.model.User;
import com.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Result> getAllUser(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<User> listUser = userService.pagination(pageNo, 5);
        if(keyword != null){
            listUser = this.userService.search(keyword, pageNo, 5);
        }
        if (!listUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list users successfully", listUser));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No users found", null));
        }
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query user successfully", user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "Cannot find user", null));
        }
    }

    @PostMapping(value = "")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Result> insertUser(@Valid @RequestBody UserDTO newUser,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        Optional<User> foundUser = userService.findByEmail(newUser.getEmail().trim());
        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Email name already taken", ""));
        }
        User userRequest = modelMapper.map(newUser, User.class);
        userRequest.setFirstName(newUser.getFirstName());
        userRequest.setLastName(newUser.getLastName());
        userRequest.setFullName(newUser.getFullName());
        userRequest.setEmail(newUser.getEmail());
        userRequest.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRequest.setGender(newUser.getGender());
        userRequest.setBirthday(newUser.getBirthday());
        userRequest.setAddress(newUser.getAddress());
        userRequest.setTelephone(newUser.getTelephone());
        userRequest.setRole(newUser.getRole());
        User user = userService.saveOrUpdate(userRequest);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert user successfully", userResponse));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Result> updateUser(@Valid @RequestBody UserDTO newUser,
                                                 BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        Optional<User> foundUser = userService.findByEmail(newUser.getEmail().trim());
        if (foundUser.isPresent() && !foundUser.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "User email already taken", ""));
        }
        User updateUser = userService.findById(id);
        if (updateUser != null) {
            updateUser.setFirstName(newUser.getFirstName());
            updateUser.setLastName(newUser.getLastName());
            updateUser.setFullName(newUser.getFullName());
            updateUser.setEmail(newUser.getEmail());
            updateUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            updateUser.setGender(newUser.getGender());
            updateUser.setBirthday(newUser.getBirthday());
            updateUser.setAddress(newUser.getAddress());
            updateUser.setTelephone(newUser.getTelephone());
            updateUser.setRole(newUser.getRole());
            updateUser = userService.saveOrUpdate(updateUser);
        } else {
            User userRequest = modelMapper.map(newUser, User.class);
            userRequest.setId(id);
            updateUser = userService.saveOrUpdate(userRequest);
        }
        UserDTO userResponse = modelMapper.map(updateUser, UserDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update user successfully", userResponse));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Result> deleteCategory(@PathVariable Integer id) {
        boolean exists = userService.existsById(id);
        if (exists) {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete user successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find user to delete", ""));
    }

}
