package com.project.service.impl;

import com.project.dto.request.CategoryDTO;
import com.project.dto.request.ImageDTO;
import com.project.dto.response.ImageViewDTO;
import com.project.dto.response.ProductViewDTO;
import com.project.exception.base.CustomException;
import com.project.model.Image;
import com.project.model.Product;
import com.project.repository.ImageRepository;
import com.project.service.ImageService;
import com.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public List<ImageViewDTO> getAll() {
        return this.imageRepository.findAll().stream()
                .map(image -> modelMapper.map(image, ImageViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ImageViewDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<Image> images;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize);
            images = this.imageRepository.findAll(pageable);
        } else {
            List<Image> list = this.imageRepository.listByName(keyword);
            pageable = PageRequest.of(pageNo-1, pageSize);
            int start = (int) pageable.getOffset();
            int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
            list = list.subList(start, end);
            images = new PageImpl<>(list, pageable, this.imageRepository.listByName(keyword).size());
        }
        if (!images.isEmpty()) {
            List<ImageViewDTO> imageDtoList = images.getContent()
                    .stream()
                    .map(image -> modelMapper.map(image, ImageViewDTO.class))
                    .collect(Collectors.toList());
            return new PageImpl<>(imageDtoList, pageable, images.getTotalElements());
        }
        return null;
    }

    @Override
    public ImageViewDTO findById(Integer id) {
        Image image = this.imageRepository.findById(id).orElse(null);
        if(image == null) {
            throw new CustomException.NotFoundException("Image not found with id : " + id, 404, new Date());
        }
        return modelMapper.map(image, ImageViewDTO.class);
    }

    @Override
    public List<ImageViewDTO> findByName(String name) {
        if(name.isEmpty()){
            throw new CustomException.NotFoundException("Name image not empty", 404, new Date());
        }
        List<Image> list = this.imageRepository.findByName(name);
        return list.stream().map(e -> modelMapper.map(e, ImageViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ImageViewDTO save(ImageDTO imageDto) {
        ProductViewDTO product = this.productService.findById(imageDto.getProductId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Image imageRequest = modelMapper.map(imageDto, Image.class);
        imageRequest.setProduct(modelMapper.map(product, Product.class));
        Image image = this.imageRepository.save(imageRequest);
        return modelMapper.map(image, ImageViewDTO.class);
    }

    @Override
    public ImageViewDTO update(ImageDTO imageDto, Integer id) {
        ProductViewDTO product = this.productService.findById(imageDto.getProductId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Image image = modelMapper.map(this.findById(id), Image.class);
        image.setName(imageDto.getName());
        image.setProduct(modelMapper.map(product, Product.class));
        image = this.imageRepository.save(image);
        return modelMapper.map(image, ImageViewDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Image image = this.imageRepository.findById(id).orElse(null);
        if(image == null) {
            throw new CustomException.NotFoundException("Image not found with id : " + id, 404, new Date());
        } else{
            this.imageRepository.delete(image);
        }
    }
}
