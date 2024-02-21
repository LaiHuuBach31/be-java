package com.project.service.impl;

import com.project.dto.request.ItemDTO;
import com.project.exception.base.CustomException;
import com.project.model.Category;
import com.project.model.Item;
import com.project.model.Product;
import com.project.repository.ItemRepository;
import com.project.repository.ProductRepository;
import com.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<ItemDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<Item> items;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize);
            items = this.itemRepository.findAll(pageable);
        } else {
            List<Item> list = this.itemRepository.listByName(keyword);
            pageable = PageRequest.of(pageNo-1, pageSize);
            int start = (int) pageable.getOffset();
            int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
            list = list.subList(start, end);
            items = new PageImpl<>(list, pageable, list.size());
        }
        if (!items.isEmpty()) {
            List<ItemDTO> itemDTOList = items.getContent()
                    .stream()
                    .map(item -> modelMapper.map(item, ItemDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(itemDTOList, pageable, items.getTotalElements());
        }
        return null;
    }

    @Override
    public ItemDTO findById(Integer id) {
        Item item = this.itemRepository.findById(id).orElse(null);
        if(item == null) {
            throw new CustomException.NotFoundException("Item not found with id : " + id, 404, new Date());
        }
        return modelMapper.map(item, ItemDTO.class);
    }

    @Override
    public List<ItemDTO> findByName(String name) {
        if(name.isEmpty()){
            throw new CustomException.NotFoundException("Name item not empty", 404, new Date());
        }
        List<Item> list = itemRepository.findByName(name);
        return list.stream().map(i -> modelMapper.map(i, ItemDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ItemDTO save(ItemDTO itemDto) {
        this.checkUnique(itemDto.getName().trim());
        Item itemRequest = modelMapper.map(itemDto, Item.class);
        Item item = this.itemRepository.save(itemRequest);
        return modelMapper.map(item, ItemDTO.class);
    }

    @Override
    public ItemDTO update(ItemDTO itemDto, Integer id) {
        this.checkUnique(itemDto.getName().trim());
        Item item = modelMapper.map(this.findById(id), Item.class);
        item.setName(itemDto.getName());
        item.setStatus(itemDto.getStatus());
        item = this.itemRepository.save(item);
        return modelMapper.map(item, ItemDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Item item = this.itemRepository.findById(id).orElse(null);
        if(item == null) {
            throw new CustomException.NotFoundException("Item not found with id : " + id, 404, new Date());
        } else{
            List<Product> list = this.productRepository.checkInItem(id);
            if(!list.isEmpty()){
                if(!check){
                    throw new CustomException.NotImplementedException("This item contains products", 501, new Date());
                } else {
                    this.productRepository.deleteAll(list);
                }
            } else {
                this.itemRepository.delete(item);
            }
        }
    }

    private void checkUnique(String name){
        List<Item> foundItem = this.itemRepository.findByName(name);
        if (!foundItem.isEmpty()) {
            throw  new CustomException.NotImplementedException("Item name already taken", 501, new Date());
        }
    }

}
