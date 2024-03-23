package com.project.service.impl;

import com.project.dto.request.UserDTO;
import com.project.exception.base.CustomException;
import com.project.model.Token;
import com.project.model.User;
import com.project.repository.TokenRepository;
import com.project.repository.UserRepository;
import com.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAll() {
        return this.userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<User> users;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize);
            users = this.userRepository.findAll(pageable);
        } else {
            List<User> list = this.userRepository.listByFullName(keyword);
            if(list.isEmpty()){
                throw new CustomException.NotFoundException("User not found with name : " + keyword, 404, new Date());
            } else{
                pageable = PageRequest.of(pageNo-1, pageSize);
                int start = (int) pageable.getOffset();
                int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
                list = list.subList(start, end);
                users = new PageImpl<>(list, pageable, this.userRepository.listByFullName(keyword).size());
            }

        }
        if (!users.isEmpty()) {
            List<UserDTO> userDtoList = users.getContent()
                    .stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(userDtoList, pageable, users.getTotalElements());
        }
        return null;
    }

    @Override
    public UserDTO findById(Integer id) {
        User user = this.userRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("User not found with id : " + id, 404, new Date()));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> findByName(String name) {
        if(name.isEmpty()){
            throw new CustomException.NotFoundException("Name user not empty", 404, new Date());
        }
        List<User> list = this.userRepository.findByFullName(name);
        return list.stream().map(e -> modelMapper.map(e, UserDTO.class)).collect(Collectors.toList());
    }

    @Override
    public UserDTO save(UserDTO userDto) {
        this.checkUnique(userDto.getEmail().trim());
        User userRequest = modelMapper.map(userDto, User.class);
        User user = this.userRepository.save(userRequest);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO update(UserDTO userDto, Integer id) {
        this.checkUnique(userDto.getEmail().trim());
        User user = modelMapper.map(this.findById(id), User.class);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setGender(userDto.getGender());
        user.setBirthday(userDto.getBirthday());
        user.setAddress(userDto.getAddress());
        user.setTelephone(userDto.getTelephone());
        user.setRole(userDto.getRole());
        user = this.userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        User user = this.userRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("User not found with id : " + id, 404, new Date()));
        List<Token> list = this.tokenRepository.checkInUser(id);
        if(!list.isEmpty()){
            if(!check){
                throw new CustomException.NotImplementedException("This user contains token", 501, new Date());
            } else {
                this.tokenRepository.deleteAll(list);
            }
        } else {
            this.userRepository.delete(user);
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(()->new CustomException.NotFoundException("User not found with email : " + email , 404, new Date()));
        return modelMapper.map(user, UserDTO.class);
    }

    private void checkUnique(String email){
        Optional<User> foundUser = this.userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            throw  new CustomException.NotImplementedException("Email already taken", 501, new Date());
        }
    }
}
