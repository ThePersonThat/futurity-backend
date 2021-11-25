package com.alex.futurity.userserver.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
    private int maxSize;
    private int minSize;

    @Override
    public void initialize(FileSize file) {
        this.maxSize = file.max();
        this.minSize = file.min();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        long fileSize = file.getSize();

        return minSize < fileSize || maxSize > fileSize;
    }
}
