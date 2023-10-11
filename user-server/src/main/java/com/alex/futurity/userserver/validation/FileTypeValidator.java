package com.alex.futurity.userserver.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {
    private List<String> types;

    @Override
    public void initialize(FileType fileType) {
        this.types = List.of(fileType.types());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(file.getOriginalFilename())
                .filter(filename -> filename.contains("."))
                .map(this::getExtension)
                .map(extension -> types.contains(extension))
                .orElse(false);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
