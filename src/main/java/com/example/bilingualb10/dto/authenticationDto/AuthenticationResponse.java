package com.example.bilingualb10.dto.authenticationDto;

import com.example.bilingualb10.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AuthenticationResponse {
    private Long id;
    private String token;
    private String email;
    private Role role;
}