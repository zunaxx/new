package com.example.bilingualb10.service;

import com.example.bilingualb10.dto.authenticationDto.SignInRequest;
import com.example.bilingualb10.dto.authenticationDto.SignUpRequest;
import com.example.bilingualb10.dto.authenticationDto.AuthenticationResponse;
import com.example.bilingualb10.enums.Role;
import com.google.firebase.auth.FirebaseAuthException;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest signUpRequest);
    AuthenticationResponse signIn(SignInRequest signInRequest);
    AuthenticationResponse authWithGoogle(String tokenId) throws FirebaseAuthException;
    void checkAuthentication(Role role);
}