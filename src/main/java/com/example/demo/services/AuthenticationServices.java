package com.example.demo.services;

import com.example.demo.controller.AuthenticationRequest;
import com.example.demo.controller.AuthenticationResponse;
import com.example.demo.controller.RegisterRequest;
import com.example.demo.user.Role;
import com.example.demo.user.User;
import com.example.demo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServices {


    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("--------------------------------------------------------------------------asdkjslakjdlksa ---");
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        log.info("new user generated with username: " + user.getUsername());
        repo.save(user);
        var jwtToken = jwtService.generatedToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new TestingAuthenticationToken(
                   request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repo.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generatedToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
