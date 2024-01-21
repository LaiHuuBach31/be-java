package com.project.service.impl;

import com.project.model.Item;
import com.project.repository.ItemRepository;
import com.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    @Override
    public List<Item> getAll() {
        return this.itemRepository.findAll();
    }

    @Override
    public Item findById(Integer id) {
        return this.itemRepository.findById(id).orElse(null);
    }

    @Override
    public List<Item> findByName(String name) {
        return this.itemRepository.findByName(name);
    }

    @Override
    public Item saveOrUpdate(Item item) {
        return this.itemRepository.save(item);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.itemRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        this.itemRepository.delete(this.findById(id));
    }

    @Override
    public Page<Item> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.itemRepository.findAll(pageable);
    }

    @Override
    public List<Item> search(String keyWord) {
        return this.itemRepository.listByName(keyWord);
    }

    @Override
    public Page<Item> search(String keyWord, Integer pageNo, Integer pageSize) {
        List<Item> list = this.search(keyWord);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        int start = (int) pageable.getOffset();
        int end = pageable.getOffset() + pageable.getPageSize() > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Item>(list, pageable, list.size());
    }
}
