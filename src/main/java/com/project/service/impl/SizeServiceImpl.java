package com.project.service.impl;

import com.project.model.Size;
import com.project.repository.SizeRepository;
import com.project.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;

    @Override
    public List<Size> getAll() {
        return this.sizeRepository.findAll();
    }

    @Override
    public Size findById(Integer id) {
        return this.sizeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Size> findByName(String name) {
        return this.sizeRepository.findByName(name);
    }

    @Override
    public Size saveOrUpdate(Size size) {
        return this.sizeRepository.save(size);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.sizeRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        this.sizeRepository.delete(this.findById(id));
    }

    @Override
    public Page<Size> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.sizeRepository.findAll(pageable);
    }

    @Override
    public List<Size> search(String keyWord) {
        return this.sizeRepository.listByName(keyWord);
    }

    @Override
    public Page<Size> search(String keyWord, Integer pageNo, Integer pageSize) {
        List<Size> list = this.search(keyWord);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        int start = (int) pageable.getOffset();
        int end = pageable.getOffset() + pageable.getPageSize() > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Size>(list, pageable, list.size());
    }

}
