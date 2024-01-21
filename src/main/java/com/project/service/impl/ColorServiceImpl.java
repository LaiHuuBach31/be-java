package com.project.service.impl;

import com.project.model.Color;
import com.project.repository.ColorRepository;
import com.project.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    @Override
    public List<Color> getAll() {
        return this.colorRepository.findAll();
    }

    @Override
    public Color findById(Integer id) {
        return this.colorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Color> findByName(String name) {
        return this.colorRepository.findByName(name);
    }

    @Override
    public Color saveOrUpdate(Color color) {
        return this.colorRepository.save(color);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.colorRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        this.colorRepository.delete(this.findById(id));
    }

    @Override
    public Page<Color> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.colorRepository.findAll(pageable);
    }

    @Override
    public List<Color> search(String keyWord) {
        return this.colorRepository.listByName(keyWord);
    }

    @Override
    public Page<Color> search(String keyWord, Integer pageNo, Integer pageSize) {
        List<Color> list = this.search(keyWord);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        int start = (int) pageable.getOffset();
        int end = pageable.getOffset() + pageable.getPageSize() > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Color>(list, pageable, list.size());
    }
}
