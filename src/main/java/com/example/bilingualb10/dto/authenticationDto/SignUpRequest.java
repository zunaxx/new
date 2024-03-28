package com.example.bilingualb10.dto.authenticationDto;

import com.example.bilingualb10.validation.EmailValid;
import com.example.bilingualb10.validation.FullNameValid;
import com.example.bilingualb10.validation.PasswordValid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SignUpRequest {
    @FullNameValid
    private String firstName;
    @FullNameValid
    private String lastName;
    @EmailValid
    private String email;
    @PasswordValid
    private String password;
}