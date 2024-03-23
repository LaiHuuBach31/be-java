package com.project.dto.response;

import com.project.model.Role;
import com.project.model.Token;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserViewDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Integer enabled;
    private Integer gender;
    private Date birthday;
    private String address;
    private String telephone;
    private Role role;
    private Set<Token> tokens;
}
