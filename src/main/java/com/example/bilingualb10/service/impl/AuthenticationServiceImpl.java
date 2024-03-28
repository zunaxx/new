package com.example.bilingualb10.service.impl;

import com.example.bilingualb10.config.jwtConfig.JwtService;
import com.example.bilingualb10.dto.authenticationDto.SignInRequest;
import com.example.bilingualb10.dto.authenticationDto.SignUpRequest;
import com.example.bilingualb10.dto.authenticationDto.AuthenticationResponse;
import com.example.bilingualb10.entity.User;
import com.example.bilingualb10.enums.Role;
import com.example.bilingualb10.globalException.AccessDeniedException;
import com.example.bilingualb10.globalException.AlreadyExistsException;
import com.example.bilingualb10.globalException.BadCredentialException;
import com.example.bilingualb10.globalException.NotFoundException;
import com.example.bilingualb10.repository.UserRepository;
import com.example.bilingualb10.service.AuthenticationService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;

@RequiredArgsConstructor
@Service
@Builder
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new AlreadyExistsException(
                    String.format("Already exists user with email: %s ", signUpRequest.getEmail()));
        }
        User user = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        log.info("User successfully saved");
        String token = jwtService.generateToken(user);
        log.info("Users token successfully generated in signUp");
        return AuthenticationResponse.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getUserByEmail(signInRequest.getEmail())
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("User with email: %s ", signInRequest.getEmail())));
        if (signInRequest.getEmail().isBlank()) {
            log.error("Email is blank");
            throw new BadCredentialException("Email is blank!");
        }
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            log.error("Wrong password");
            throw new BadCredentialException("Wrong password!");
        }
        String token = jwtService.generateToken(user);
        log.info("Token successfully generated in signIn");
        return AuthenticationResponse.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @PostConstruct
    public void initSaveAdmin() {
        User user = User.builder()
                .firstName("admin")
                .lastName("admins lastName")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("Admin123"))
                .role(Role.ADMIN)
                .build();
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
        }
        log.info("Admin successfully created and saved");
    }

    @PostConstruct
    void init() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebaseProperties.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions);
    }

    @Override
    public AuthenticationResponse authWithGoogle(String tokenId) throws FirebaseAuthException{
        log.info("auth with google has been started");
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
        User user;
        if (!userRepository.existsByEmail(firebaseToken.getEmail())) {
            User newUser = new User();
            String[] names = firebaseToken.getName().split(" ");
            log.info(String.valueOf(names.length));
            newUser.setFirstName(names[0]);
            newUser.setLastName(names[1]);
            newUser.setEmail(firebaseToken.getEmail());
            newUser.setPassword(passwordEncoder.encode(firebaseToken.getEmail()));
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
        }
        user = userRepository.getUserByEmail(firebaseToken.getEmail()).orElseThrow(() -> {
            log.info(String.format("Not found user with email %s!",
                    firebaseToken.getEmail()));
            throw new NotFoundException(
                    String.format("Not found user with email %s",
                            firebaseToken.getEmail()));
        });
        String token = jwtService.generateToken(user);
        log.info("auth with google has been ended");
        return AuthenticationResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }


    @Override
    public void checkAuthentication(Role role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required to create a answers !!!");
        }
        String email = authentication.getName();
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> {
            log.error("User with email is not found !!!");
            return new NotFoundException("User is not found !!!");
        });
        if (user.getRole().equals(role)) {
            throw new AccessDeniedException(
                    role.equals(Role.USER)
                            ? "Authentication required to be ADMIN to create a answers !!! tokendi kara guildir bash"
                            : "Authentication required to be USER to create a answers !!! tokendi kara guildir bash" );
        }
    }
}