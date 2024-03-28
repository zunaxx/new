package com.example.bilingualb10.validation.valid;

import com.example.bilingualb10.validation.FullNameValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FullNameValidation  implements ConstraintValidator<FullNameValid, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() >= 2 && s.matches("^[a-zA-Z]+$");
    }
}