package com.project.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.config.JwtService;
import com.project.dto.request.TokenDTO;
import com.project.dto.request.UserDTO;
import com.project.model.*;
import com.project.service.TokenService;
import com.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .address(request.getAddress())
                .telephone(request.getTelephone())
                .enabled(1)
                .role(Role.USER)
                .build();
        UserDTO savedUser = userService.save(modelMapper.map(user, UserDTO.class));
        CustomUserDetails userDetails = convertToCustomerUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
//        saveUserToken(modelMapper.map(savedUser, User.class), jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(null)
                .refreshToken(null)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userService.findByEmail(request.getEmail());
        CustomUserDetails userDetails = convertToCustomerUserDetails(modelMapper.map(user, User.class));
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        revokeAllUserTokens(modelMapper.map(user, User.class));
        saveUserToken(modelMapper.map(user, User.class), refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(modelMapper.map(token, TokenDTO.class));
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAllToken(validUserTokens);
    }
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        String checkToken = tokenService.findByToken(refreshToken).toString();
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null && checkToken.isEmpty()) {
            var user = userService.findByEmail(userEmail);
            CustomUserDetails userDetails = convertToCustomerUserDetails(modelMapper.map(user, User.class));
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                var newRefreshToken = jwtService.generateRefreshToken(userDetails);
                revokeAllUserTokens(modelMapper.map(user, User.class));
                saveUserToken(modelMapper.map(user, User.class), accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    private CustomUserDetails convertToCustomerUserDetails(User user) {
        return CustomUserDetails.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .address(user.getAddress())
                .telephone(user.getTelephone())
                .enabled(user.getEnabled())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authorities(user.getRole().getAuthorities())
                .build();
    }
}
