package com.example.smsrly.service;

import com.example.smsrly.config.JwtService;
import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.entity.ResetPasswordCode;
import com.example.smsrly.entity.VerificationEmailCode;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.UserRepository;
import lombok.*;
import org.springframework.http.ResponseEntity;
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


    public String register(RegisterRequest request) {

        int generatedCode = generateCode();

        Optional<User> userEmail = userRepository.findUserByEmail(request.getEmail());

        if (userEmail.isPresent() && userEmail.get().isEnabled()) {
            throw new IllegalStateException("email is already inserted into DB");
        } else if (userEmail.isPresent() && !userEmail.get().isEnabled()) {

            userService.updateUser(userEmail.get().getId(),
                    request.getFirstname(),
                    request.getLastname(),
                    request.getPassword(),
                    Optional.of(request.getPhoneNumber()),
                    Optional.of(request.getLatitude()),
                    Optional.of(request.getLongitude()),
                    request.getImage());
            return emailServices.sendEmail(userEmail.get(), generatedCode, "confirmationEmail");
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

        Optional<User> userEmail = userRepository.findUserByEmail(request.getEmail());

        if (userEmail.isEmpty()) {
            throw new IllegalStateException("email not found in DB");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(userRepository.findIdByEmail(request.getEmail()))
                .build();
    }

    @Transactional
    public AuthenticationResponse emailConfirmationCode(String code) {
        VerificationEmailCode verificationEmailCode = verificationEmailCodeService
                .getCode(code)
                .orElseThrow(() ->
                        new IllegalStateException("code not found"));

        if (verificationEmailCode.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = verificationEmailCode.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("code expired");
        }

        verificationEmailCodeService.setConfirmedAt(code);
        userRepository.enableUser(verificationEmailCode.getUser().getEmail());


        int userId = verificationEmailCodeService.getUserByVerificationEmailCode(Integer.parseInt(code));
        Optional<User> getUser = userRepository.findById(userId);

        verificationEmailCodeService.expireAllRequestsCode(getUser.get().getId());

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(getUser.get()))
                .id(getUser.get().getId())
                .build();
    }

    @Transactional
    public ResponseEntity<String> passwordConfirmationCode(String code) {
        ResetPasswordCode resetPasswordCode = resetPasswordService
                .getCode(code)
                .orElseThrow(() ->
                        new IllegalStateException("code not found"));

        if (resetPasswordCode.getExpired()) {
            return ResponseEntity.ok("code is expired");
            //throw new IllegalStateException("code is expired");
        }
        resetPasswordService.setExpired(code);

        int userId = resetPasswordService.getUserByResetPasswordCode(Integer.parseInt(code));
        Optional<User> getUser = userRepository.findById(userId);

        resetPasswordService.expireAllRequestsCode(getUser.get().getId());
        return ResponseEntity.ok("id: " + getUser.get().getId());
    }


    public String resetPassword(String userEmail) {
        int generatedCode = generateCode();
        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            return "email not exists in database";
        }

        return emailServices.sendEmail(user.get(), generatedCode, "resetPassword");
    }


    @Transactional
    public AuthenticationResponse updateUserPassword(int userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalStateException("user with id " + userId + " not exists")
        );
        user.setPassword(passwordEncoder.encode(password));
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .id(userId)
                .build();
    }

}