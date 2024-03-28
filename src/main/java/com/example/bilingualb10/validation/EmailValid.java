package com.example.bilingualb10.validation;

import com.example.bilingualb10.validation.valid.EmailValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface EmailValid {
    String message() default "Enter correct email !!!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}