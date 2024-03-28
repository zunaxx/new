package com.example.bilingualb10.validation;

import com.example.bilingualb10.validation.valid.PasswordValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface PasswordValid {
    String message() default "Password must be longer than 4 and must contain letters !!!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}