package com.example.bilingualb10.api;

import com.example.bilingualb10.dto.authenticationDto.SignInRequest;
import com.example.bilingualb10.dto.authenticationDto.SignUpRequest;
import com.example.bilingualb10.dto.authenticationDto.AuthenticationResponse;
import com.example.bilingualb10.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
@Tag(name = "authentication API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    @Operation(summary = "Метод для регистрации в Bilingual")
    public AuthenticationResponse signUp (@RequestBody @Valid SignUpRequest signUpRequest){
        return authenticationService.signUp(signUpRequest);
    }

    @PostMapping("/signIn")
    @Operation(summary = "Метод для авторизации")
    public AuthenticationResponse signIn (@RequestBody @Valid SignInRequest signInRequest){
        return authenticationService.signIn(signInRequest);
    }

    @SneakyThrows
    @PostMapping("/signInWithGoogle")
    @Operation(summary = "Метод для авторизации через Google")
    public AuthenticationResponse signUpWithGoogle(@RequestParam String token){
        return authenticationService.authWithGoogle(token);
    }
}