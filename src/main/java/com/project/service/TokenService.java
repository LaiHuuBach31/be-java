package com.project.service;

import com.project.model.Token;

import java.util.List;
import java.util.Optional;

public interface TokenService extends Generic<Token, Integer> {
    List<Token> findAllValidTokenByUser(Integer id);
    Optional<Token> findByToken(String token);
    List<Token> saveAllToken(Iterable<Token> tokens);
}
