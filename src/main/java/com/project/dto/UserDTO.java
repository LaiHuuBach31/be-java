package com.project.dto;

import com.project.model.Cart;
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
public class UserDTO {
    @NotEmpty(message = "First name is required")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Full name is required")
    private String fullName;
    @NotEmpty(message = "Email is required")
    private String email;
    @NotEmpty(message = "Password is required")
    private String password;
    private Integer enabled;
    @NotNull(message = "Gender is required")
    private Integer gender;
    @NotNull(message = "Birthday is required")
    private Date birthday;
    @NotEmpty(message = "Address is required")
    private String address;
    @NotEmpty(message = "Telephone is required")
    private String telephone;
    private Role role;
    private Set<Token> tokens;
//    private Set<Cart> carts;
}
