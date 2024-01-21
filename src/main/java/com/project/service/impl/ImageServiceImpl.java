package com.project.service.impl;

import com.project.model.Image;
import com.project.repository.ImageRepository;
import com.project.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public List<Image> getAll() {
        return this.imageRepository.findAll();
    }

    @Override
    public Image findById(Integer id) {
        return this.imageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Image> findByName(String name) {
        return this.imageRepository.findByName(name);
    }

    @Override
    public Image saveOrUpdate(Image image) {
        return this.imageRepository.save(image);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.imageRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        this.imageRepository.delete(this.findById(id));
    }

    @Override
    public Page<Image> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.imageRepository.findAll(pageable);
    }

    @Override
    public List<Image> search(String keyWord) {
        return this.imageRepository.listByName(keyWord);
    }

    @Override
    public Page<Image> search(String keyWord, Integer pageNo, Integer pageSize) {
        List<Image> list = this.search(keyWord);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        int start = (int) pageable.getOffset();
        int end = pageable.getOffset() + pageable.getPageSize() > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Image>(list, pageable, list.size());
    }
}
