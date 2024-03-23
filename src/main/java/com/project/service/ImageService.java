package com.project.service;

import com.project.dto.request.ImageDTO;
import com.project.dto.response.ImageViewDTO;
import com.project.dto.response.VariantProductViewDTO;

import java.util.List;

public interface ImageService extends Generic<ImageDTO, ImageViewDTO>{
    List<ImageViewDTO> listImage(Integer id);
}
