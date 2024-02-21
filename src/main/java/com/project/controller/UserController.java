package com.project.controller;

import com.project.dto.UserDTO;
import com.project.dto.request.CategoryDTO;
import com.project.model.Result;
import com.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Result> getAllUser(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<UserDTO> listCategory = userService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list user successfully", listCategory));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        UserDTO userDto = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query user by id successfully", userDto));
    }

    @PostMapping(value = "")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Result> insertUser(@Valid @RequestBody UserDTO newUser) {
        UserDTO userDto = userService.save(newUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert user successfully", userDto));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Result> updateUser(@Valid @RequestBody UserDTO newUser, @PathVariable Integer id) {
        UserDTO userDto = userService.update(newUser, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update user successfully", userDto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Result> deleteCategory(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.userService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete user successfully", null));
    }

}
