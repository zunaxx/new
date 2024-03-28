package com.example.bilingualb10.dto.authenticationDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SignInRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}