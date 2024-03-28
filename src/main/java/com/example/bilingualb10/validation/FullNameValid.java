package com.example.bilingualb10.validation;

import com.example.bilingualb10.validation.valid.FullNameValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FullNameValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface FullNameValid {
    String message() default "Name must be longer than 2 and must contain the english letters !!!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}