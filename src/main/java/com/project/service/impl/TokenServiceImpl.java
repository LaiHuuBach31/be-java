package com.project.service.impl;

import com.project.dto.request.TokenDTO;
import com.project.exception.base.CustomException;
import com.project.model.Token;
import com.project.repository.TokenRepository;
import com.project.service.TokenService;
import com.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public List<TokenDTO> getAll() {
        return this.tokenRepository.findAll().stream()
                .map(token -> modelMapper.map(token, TokenDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TokenDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Token> tokens = this.tokenRepository.findAll(pageable);
        List<TokenDTO> tokenDtoList = tokens.getContent()
                .stream()
                .map(e -> modelMapper.map(e, TokenDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(tokenDtoList, pageable, tokens.getTotalElements());
    }

    @Override
    public TokenDTO findById(Integer id) {
        Token token = this.tokenRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Token not found with id : " + id, 404, new Date()));
        return modelMapper.map(token, TokenDTO.class);
    }

    @Override
    public List<TokenDTO> findByName(String name) {
        return null;
    }

    @Override
    public TokenDTO save(TokenDTO tokenDto) {
        this.checkUnique(tokenDto.getToken().trim());
        Token tokenRequest = modelMapper.map(tokenDto, Token.class);
        Token token = this.tokenRepository.save(tokenRequest);
        return modelMapper.map(token, TokenDTO.class);
    }

    @Override
    public TokenDTO update(TokenDTO tokenDto, Integer id) {
        return null;
    }

    @Override
    public void delete(Integer id, boolean check) {
        Token token = this.tokenRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Token not found with id : " + id, 404, new Date()));
        this.tokenRepository.delete(token);
    }

    @Override
    public List<TokenDTO> findAllValidTokenByUser(Integer id) {
        this.userService.findById(id);
        List<Token> list = this.tokenRepository.findAllValidTokenByUser(id);
        return list.stream().map(e -> modelMapper.map(e, TokenDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<TokenDTO> findByToken(String token) {
        Optional<Token> tokenOptional = this.tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            return Optional.of(modelMapper.map(tokenOptional.get(), TokenDTO.class));
        } else {
            throw new CustomException.NotFoundException("Token not found" , 404, new Date());
        }
    }

    @Override
    public List<TokenDTO> saveAllToken(Iterable<TokenDTO> tokenDTOs) {
        List<Token> tokens = new ArrayList<>();
        for (TokenDTO tokenDTO : tokenDTOs) {
            Token token = modelMapper.map(tokenDTO, Token.class);
            tokens.add(token);
        }
        List<Token> savedTokens = tokenRepository.saveAll(tokens);
        return savedTokens.stream()
                .map(token -> modelMapper.map(token, TokenDTO.class))
                .collect(Collectors.toList());
    }

    private void checkUnique(String token){
        Optional<Token> foundToken = this.tokenRepository.findByToken(token);
        if (foundToken.isPresent()) {
            throw  new CustomException.NotImplementedException("Token name already taken", 501, new Date());
        }
    }
}
