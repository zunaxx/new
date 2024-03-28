package com.example.bilingualb10.validation.valid;

import com.example.bilingualb10.validation.PasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<PasswordValid,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() >= 4 && s.matches(".*[a-zA-Z].*\\d.*|.*\\d.*[a-zA-Z].*");
    }
}