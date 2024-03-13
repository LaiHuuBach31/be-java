package com.project.service.impl;

import com.project.dto.request.ColorDTO;
import com.project.dto.request.SizeDTO;
import com.project.exception.base.CustomException;
import com.project.model.Size;
import com.project.model.VariantProduct;
import com.project.repository.SizeRepository;
import com.project.repository.VariantProductRepository;
import com.project.service.SizeService;
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
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    private final VariantProductRepository variantProductRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SizeDTO> getAll() {
        return this.sizeRepository.findAll().stream()
                .map(size -> modelMapper.map(size, SizeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<SizeDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<Size> sizes;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize);
            sizes = this.sizeRepository.findAll(pageable);
        } else {
            List<Size> list = this.sizeRepository.listByName(keyword);
            if(list.isEmpty()){
                throw new CustomException.NotFoundException("Size not found with name : " + keyword, 404, new Date());
            } else {
                pageable = PageRequest.of(pageNo-1, pageSize);
                int start = (int) pageable.getOffset();
                int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
                list = list.subList(start, end);
                sizes = new PageImpl<>(list, pageable, this.sizeRepository.listByName(keyword).size());
            }
        }
        if (!sizes.isEmpty()) {
            List<SizeDTO> sizeDtoList = sizes.getContent()
                    .stream()
                    .map(size -> modelMapper.map(size, SizeDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(sizeDtoList, pageable, sizes.getTotalElements());
        }
        return null;
    }

    @Override
    public SizeDTO findById(Integer id) {
        Size size = this.sizeRepository.findById(id).orElse(null);
        if(size == null) {
            throw new CustomException.NotFoundException("Size not found with id : " + id, 404, new Date());
        }
        return modelMapper.map(size, SizeDTO.class);
    }

    @Override
    public List<SizeDTO> findByName(String name) {
        if(name.isEmpty()){
            throw new CustomException.NotFoundException("Name size not empty", 404, new Date());
        }
        List<Size> list = sizeRepository.findByName(name);
        return list.stream().map(e -> modelMapper.map(e, SizeDTO.class)).collect(Collectors.toList());
    }

    @Override
    public SizeDTO save(SizeDTO sizeDto) {
        this.checkUnique(sizeDto.getName().trim());
        Size sizeRequest = modelMapper.map(sizeDto, Size.class);
        Size size = this.sizeRepository.save(sizeRequest);
        return modelMapper.map(size, SizeDTO.class);
    }

    @Override
    public SizeDTO update(SizeDTO sizeDto, Integer id) {
        this.checkUnique(sizeDto.getName().trim());
        Size size = modelMapper.map(this.findById(id), Size.class);
        size.setName(sizeDto.getName());
        size.setStatus(sizeDto.getStatus());
        size = this.sizeRepository.save(size);
        return modelMapper.map(size, SizeDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Size size = this.sizeRepository.findById(id).orElse(null);
        if(size == null) {
            throw new CustomException.NotFoundException("Size not found with id : " + id, 404, new Date());
        } else{
            List<VariantProduct> list = this.variantProductRepository.checkInSize(id);
            if(!list.isEmpty()){
                if(!check){
                    throw new CustomException.NotImplementedException("This size contains in variant product", 501, new Date());
                } else {
                    this.variantProductRepository.deleteAll(list);
                }
            } else {
                this.sizeRepository.delete(size);
            }
        }
    }

    private void checkUnique(String name){
        List<Size> foundSize = this.sizeRepository.findByName(name);
        if (!foundSize.isEmpty()) {
            throw  new CustomException.NotImplementedException("Size name already taken", 501, new Date());
        }
    }
}
