package com.example.smsrly.service;

import com.example.smsrly.config.JwtService;
import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.entity.ResetPasswordCode;
import com.example.smsrly.entity.VerificationEmailCode;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.response.Response;
import lombok.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationEmailCodeService verificationEmailCodeService;
    private final ResetPasswordService resetPasswordService;
    private final UserService userService;
    private final EmailServices emailServices;


    public int generateCode() {
        Random random = new Random();
        int code = 0;
        while (code < 1000) {
            code = random.nextInt(10000);
        }
        return code;
    }


    public Response register(RegisterRequest request) {

        int generatedCode = generateCode();

        Optional<User> userEmail = userRepository.findUserByEmail(request.getEmail());

        if (userEmail.isPresent() && userEmail.get().isEnabled()) {
            return Response.builder().message("email is already inserted into DB").build();
        } else if (userEmail.isPresent() && !userEmail.get().isEnabled() && verificationEmailCodeService.checkIfCodeExpiredOrNot(userEmail.get().getId()).isEmpty()) {

            userService.updateUser(null,
                    userEmail.get().getEmail(),
                    request.getFirstname(),
                    request.getLastname(),
                    request.getPassword(),
                    Optional.of(request.getPhoneNumber()),
                    Optional.of(request.getLatitude()),
                    Optional.of(request.getLongitude()),
                    request.getImage());

            return emailServices.sendEmail(userEmail.get(), generatedCode, "confirmationEmail");
        } else if (userEmail.isPresent() && !userEmail.get().isEnabled() && verificationEmailCodeService.checkIfCodeExpiredOrNot(userEmail.get().getId()).isPresent()) {
            return Response.builder().message("try again after 15 min").build();
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
        userRepository.save(user);

        return emailServices.sendEmail(user, generatedCode, "confirmationEmail");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Optional<User> user = userRepository.findUserByEmail(request.getEmail());

        if (user.isEmpty()) {
            return AuthenticationResponse.builder().message("email not found in DB").build();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user.get());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse emailConfirmationCode(String email, int code) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("user not found"));

        Optional<VerificationEmailCode> verificationEmailCode = verificationEmailCodeService
                .getCode(code, user.getId());

        if (verificationEmailCode.isEmpty()) {
            return AuthenticationResponse.builder().message("code is invalid").build();
        }

        if (verificationEmailCode.get().getConfirmedAt() != null) {
            return AuthenticationResponse.builder()
                    .message("email already confirmed")
                    .build();
        }

        LocalDateTime expiredAt = verificationEmailCode.get().getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return AuthenticationResponse.builder()
                    .message("code expired")
                    .build();
        }

        verificationEmailCodeService.setConfirmedAt(code, user.getId());
        userRepository.enableUser(verificationEmailCode.get().getUser().getEmail());


        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .message("activated")
                .build();
    }

    @Transactional
    public AuthenticationResponse passwordConfirmationCode(String email, int code) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("user not found"));

        Optional<ResetPasswordCode> resetPasswordCode = resetPasswordService
                .getCode(code, user.getId());

        if (resetPasswordCode.isEmpty()) {
            return AuthenticationResponse.builder().message("code is invalid").build();
        }

        if (resetPasswordCode.get().getExpired()) {
            return AuthenticationResponse.builder().message("code is expired").build();
        }

        resetPasswordService.setExpired(code, user.getId());

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }


    public Response resetPassword(String userEmail) {
        int generatedCode = generateCode();
        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            return Response.builder().message("email not exists in database").build();
        }

        resetPasswordService.expireAllRequestedCode(user.get().getId());

        return emailServices.sendEmail(user.get(), generatedCode, "resetPassword");
    }


    @Transactional
    public Response updateUserPassword(String authHeader, String password) {
        User user = userService.getUser(authHeader);
        user.setPassword(passwordEncoder.encode(password));
        return Response.builder().message("password updated").build();
    }

}