package com.project.service;

import com.project.dto.request.TokenDTO;
import com.project.model.Token;

import java.util.List;
import java.util.Optional;

public interface TokenService extends Generic<TokenDTO, TokenDTO> {
    List<TokenDTO> findAllValidTokenByUser(Integer id);
    Optional<TokenDTO> findByToken(String token);
    List<TokenDTO> saveAllToken(Iterable<TokenDTO> tokens);
}
