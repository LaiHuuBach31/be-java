package com.project.service.impl;

import com.project.model.Token;
import com.project.repository.TokenRepository;
import com.project.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public List<Token> findAllValidTokenByUser(Integer id) {
        return this.tokenRepository.findAllValidTokenByUser(id);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public List<Token> saveAllToken(Iterable<Token> tokens) {
        return tokenRepository.saveAll(tokens);
    }


    @Override
    public List<Token> getAll() {
        return this.tokenRepository.findAll();
    }

    @Override
    public Token findById(Integer key) {
        return null;
    }

    @Override
    public List<Token> findByName(String name) {
        return null;
    }

    @Override
    public Token saveOrUpdate(Token object) {
        return this.tokenRepository.save(object);
    }

    @Override
    public boolean existsById(Integer key) {
        return false;
    }

    @Override
    public void delete(Integer key) {

    }

    @Override
    public Page<Token> pagination(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public List<Token> search(String keyWord) {
        return null;
    }

    @Override
    public Page<Token> search(String keyWord, Integer pageNo, Integer pageSize) {
        return null;
    }
}
