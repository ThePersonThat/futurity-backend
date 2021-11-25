package com.alex.futurity.userserver.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileEmptyValidator implements ConstraintValidator<FileNotEmpty, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null) {
            return false;
        }

        return !file.isEmpty();
    }
}
