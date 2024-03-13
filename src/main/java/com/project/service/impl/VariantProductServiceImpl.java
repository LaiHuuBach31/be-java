package com.project.service.impl;

import com.project.dto.request.ColorDTO;
import com.project.dto.request.SizeDTO;
import com.project.dto.request.VariantProductDTO;
import com.project.dto.response.ProductViewDTO;
import com.project.dto.response.VariantProductViewDTO;
import com.project.exception.base.CustomException;
import com.project.model.*;
import com.project.repository.VariantProductRepository;
import com.project.service.ColorService;
import com.project.service.ProductService;
import com.project.service.SizeService;
import com.project.service.VariantProductService;
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
public class VariantProductServiceImpl implements VariantProductService {

    private final VariantProductRepository variantProductRepository;
    private final ProductService productService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final ModelMapper modelMapper;

    @Override
    public List<VariantProductViewDTO> getAll() {
        return null;
    }

    @Override
    public Page<VariantProductViewDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<VariantProduct> variantProducts = this.variantProductRepository.findAll(pageable);
        List<VariantProductViewDTO> variantProductDTOList = variantProducts.getContent()
                .stream()
                .map(e -> modelMapper.map(e, VariantProductViewDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(variantProductDTOList, pageable, variantProducts.getTotalElements());
    }

    @Override
    public VariantProductViewDTO findById(Integer id) {
        VariantProduct variantProduct = this.variantProductRepository.findById(id).orElse(null);
        if(variantProduct == null) {
            throw new CustomException.NotFoundException("Variant Product not found with id : " + id, 404, new Date());
        }
        return modelMapper.map(variantProduct, VariantProductViewDTO.class);
    }

    @Override
    public List<VariantProductViewDTO> findByName(String name) {
        return null;
    }

    @Override
    public VariantProductViewDTO save(VariantProductDTO variantProductDto) {
        VariantProduct checkAVariantProduct = this.variantProductRepository.checkAttributes(variantProductDto.getProductId(), variantProductDto.getColorId(), variantProductDto.getSizeId());
        if(checkAVariantProduct != null){
            checkAVariantProduct.setQuantity(checkAVariantProduct.getQuantity() + variantProductDto.getQuantity());
            return modelMapper.map(this.variantProductRepository.save(checkAVariantProduct),VariantProductViewDTO.class);
        }
        ProductViewDTO productViewDTO = this.productService.findById(variantProductDto.getProductId());
        ColorDTO colorDTO = this.colorService.findById(variantProductDto.getColorId());
        SizeDTO sizeDTO = this.sizeService.findById(variantProductDto.getSizeId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        VariantProduct variantProductRequest = modelMapper.map(variantProductDto, VariantProduct.class);
        variantProductRequest.setProduct(modelMapper.map(productViewDTO, Product.class));
        variantProductRequest.setColor(modelMapper.map(colorDTO, Color.class));
        variantProductRequest.setSize(modelMapper.map(sizeDTO, Size.class));
        VariantProduct variantProduct = this.variantProductRepository.save(variantProductRequest);
        return modelMapper.map(variantProduct, VariantProductViewDTO.class);
    }

    @Override
    public VariantProductViewDTO update(VariantProductDTO variantProductDto, Integer id) {
        ProductViewDTO productViewDTO = this.productService.findById(variantProductDto.getProductId());
        ColorDTO colorDTO = this.colorService.findById(variantProductDto.getColorId());
        SizeDTO sizeDTO = this.sizeService.findById(variantProductDto.getSizeId());
        VariantProduct variantProduct = modelMapper.map(this.findById(id), VariantProduct.class);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        variantProduct.setProduct(modelMapper.map(productViewDTO, Product.class));
        variantProduct.setColor(modelMapper.map(colorDTO, Color.class));
        variantProduct.setSize(modelMapper.map(sizeDTO, Size.class));
        variantProduct.setQuantity(variantProductDto.getQuantity());
        variantProduct = this.variantProductRepository.save(variantProduct);
        return modelMapper.map(variantProduct, VariantProductViewDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        VariantProduct variantProduct = this.variantProductRepository.findById(id).orElse(null);
        if(variantProduct == null) {
            throw new CustomException.NotFoundException("Variant Product not found with id : " + id, 404, new Date());
        } else{
            this.variantProductRepository.delete(variantProduct);
        }
    }

}
