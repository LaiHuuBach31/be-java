package com.project.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    private String[] types;

    @Override
    public void initialize(ValidImage validImage) {
        this.types = validImage.type();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        boolean bl = false;

        if(value.getOriginalFilename().isEmpty()) {
            bl = true;
        }
        for (String type : types) {
            if(type.equals(value.getContentType())) {
                return true;
            }
        }

        return bl;
    }
}
