package com.example.smsrly.service;

import com.example.smsrly.config.JwtService;
import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.entity.ConfirmationCode;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationCodeService confirmationCodeService;

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
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .image(request.getImage())
                .enable(false)
                .build();
        repository.save(user);
//        var jwtToken = jwtService.generateToken(user);

        String verificationCode = UUID.randomUUID().toString();

        ConfirmationCode confirmationCode = new ConfirmationCode(
                verificationCode,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
        confirmationCodeService.saveVerificationCode(confirmationCode);

        return AuthenticationResponse.builder()
                .massage("Verification email sent " + verificationCode)
//                .token(jwtToken)
//                .id(user.getId())
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

    @Transactional
    public String confirmCode(String code) {
        ConfirmationCode confirmationCode = confirmationCodeService
                .getCode(code)
                .orElseThrow(() ->
                        new IllegalStateException("code not found"));

        if (confirmationCode.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationCode.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("code expired");
        }

        confirmationCodeService.setConfirmedAt(code);
        repository.enableUser(confirmationCode.getUser().getEmail());
        return "confirmed";
    }


}