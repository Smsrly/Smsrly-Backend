package com.example.smsrly.service;

import com.example.smsrly.config.JwtService;
import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> userEmail = repository.findUserByEmail(request.getEmail());
        Optional<User> userPhoneNumber = repository.findUserByPhoneNumber(request.getPhoneNumber());

        if (userEmail.isPresent() || userPhoneNumber.isPresent()) {
            throw new IllegalStateException("email or phone number is already inserted into DB");
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Optional<User> userEmail = repository.findUserByEmail(request.getEmail());

        if (userEmail.isEmpty()) {
            throw new IllegalStateException("email not found in DB");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findUserByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(repository.findIdByEmail(request.getEmail()))
                .build();
    }


}