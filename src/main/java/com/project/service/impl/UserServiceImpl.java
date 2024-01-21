package com.project.service.impl;

import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findById(Integer key) {
        return this.userRepository.findById(key).orElse(null);
    }

    @Override
    public List<User> findByName(String name) {
        return null;
    }

    @Override
    public User saveOrUpdate(User object) {
        return this.userRepository.save(object);
    }

    @Override
    public boolean existsById(Integer key) {
        return this.userRepository.existsById(key);
    }

    @Override
    public void delete(Integer key) {
        this.userRepository.delete(findById(key));
    }

    @Override
    public Page<User> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.userRepository.findAll(pageable);
    }

    @Override
    public List<User> search(String keyWord) {
        return this.userRepository.listByFullName(keyWord);
    }

    @Override
    public Page<User> search(String keyWord, Integer pageNo, Integer pageSize) {
        List<User> list = this.search(keyWord);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        int start = (int) pageable.getOffset();
        int end = pageable.getOffset() + pageable.getPageSize() > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<User>(list, pageable, list.size());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

}
