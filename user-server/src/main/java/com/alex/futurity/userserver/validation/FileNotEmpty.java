package com.alex.futurity.userserver.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileEmptyValidator.class)
public @interface FileNotEmpty {
    String message() default "com.alex.futurity.userserver.validation.FileNotEmpty";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
