package com.project.service.impl;

import com.project.dto.request.ColorDTO;
import com.project.exception.base.CustomException;
import com.project.model.Category;
import com.project.model.Color;
import com.project.model.Product;
import com.project.model.VariantProduct;
import com.project.repository.ColorRepository;
import com.project.repository.ProductRepository;
import com.project.repository.VariantProductRepository;
import com.project.service.ColorService;
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
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final VariantProductRepository variantProductRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<ColorDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<Color> colors;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize);
            colors = this.colorRepository.findAll(pageable);
        } else {
            List<Color> list = this.colorRepository.listByName(keyword);
            pageable = PageRequest.of(pageNo-1, pageSize);
            int start = (int) pageable.getOffset();
            int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
            list = list.subList(start, end);
            colors = new PageImpl<>(list, pageable, list.size());
        }
        if (!colors.isEmpty()) {
            List<ColorDTO> colorDtoList = colors.getContent()
                    .stream()
                    .map(color -> modelMapper.map(color, ColorDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(colorDtoList, pageable, colors.getTotalElements());
        }
        return null;
    }

    @Override
    public ColorDTO findById(Integer id) {
        Color color = this.colorRepository.findById(id).orElse(null);
        if(color == null) {
            throw new CustomException.NotFoundException("Color not found with id : " + id, 404, new Date());
        }
        return modelMapper.map(color, ColorDTO.class);
    }

    @Override
    public List<ColorDTO> findByName(String name) {
        if(name.isEmpty()){
            throw new CustomException.NotFoundException("Name color not empty", 404, new Date());
        }
        List<Color> list = colorRepository.findByName(name);
        return list.stream().map(e -> modelMapper.map(e, ColorDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ColorDTO save(ColorDTO colorDto) {
        this.checkUnique(colorDto.getName().trim());
        Color colorRequest = modelMapper.map(colorDto, Color.class);
        Color color = this.colorRepository.save(colorRequest);
        return modelMapper.map(color, ColorDTO.class);
    }

    @Override
    public ColorDTO update(ColorDTO colorDto, Integer id) {
        this.checkUnique(colorDto.getName().trim());
        Color color = modelMapper.map(this.findById(id), Color.class);
        color.setName(colorDto.getName());
        color.setStatus(colorDto.getStatus());
        color = this.colorRepository.save(color);
        return modelMapper.map(color, ColorDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Color color = this.colorRepository.findById(id).orElse(null);
        if(color == null) {
            throw new CustomException.NotFoundException("Color not found with id : " + id, 404, new Date());
        } else{
            List<VariantProduct> list = this.variantProductRepository.checkInColor(id);
            if(!list.isEmpty()){
                if(!check){
                    throw new CustomException.NotImplementedException("This color contains in variant product", 501, new Date());
                } else {
                    this.variantProductRepository.deleteAll(list);
                }
            } else {
                this.colorRepository.delete(color);
            }
        }
    }

    private void checkUnique(String name){
        List<Color> foundColor = this.colorRepository.findByName(name);
        if (!foundColor.isEmpty()) {
            throw  new CustomException.NotImplementedException("Color name already taken", 501, new Date());
        }
    }
}
