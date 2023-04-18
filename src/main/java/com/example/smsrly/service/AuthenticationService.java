package com.example.smsrly.service;

import com.example.smsrly.auth.AuthorizationRequest;
import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.entity.ResetPasswordCode;
import com.example.smsrly.entity.Token;
import com.example.smsrly.entity.VerificationEmailCode;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.TokenRepository;
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
import java.util.UUID;

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
    private final TokenRepository tokenRepository;
    private final ValidatingService validatingService;

    public int generateCode() {
        Random random = new Random();
        int code = 0;
        while (code < 1000) {
            code = random.nextInt(10000);
        }
        return code;
    }

    private void saveToken(User user, String generateToken) {
        var token = Token.builder()
                .token(generateToken)
                .isExpired(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    private void expireAllTokens(User user) {
        var userValidToken = tokenRepository.getAllValidTokens(user.getId());
        if (userValidToken.isEmpty()) return;

        userValidToken.forEach(token -> token.setExpired(true));

    }


    public Response register(RegisterRequest request) {

        int generatedCode = generateCode();

        Optional<User> userEmail = userRepository.findUserByEmail(request.getEmail());

        if (userEmail.isPresent() && userEmail.get().isEnabled()) {
            return Response.builder().message("email is already inserted into DB").build();
        }

        String validationMessage = validatingService.validating(request.getFirstname(), request.getLastname(), request.getEmail(), request.getPassword(), request.getPhoneNumber(), request.getLatitude(), request.getLongitude(), null, 0);
        if (validationMessage != "validated") {
            return Response.builder().message(validationMessage).build();
        }

        if (userEmail.isPresent() && !userEmail.get().isEnabled() && verificationEmailCodeService.checkIfCodeExpiredOrNot(userEmail.get().getId()).isEmpty()) {

            userService.updateUser(null,
                    userEmail.get().getEmail(),
                    request.getFirstname().replaceAll("\\s", ""),
                    request.getLastname().replaceAll("\\s", ""),
                    request.getPassword(),
                    Optional.of(request.getPhoneNumber()),
                    Optional.of(request.getLatitude()),
                    Optional.of(request.getLongitude()),
                    null);

            return emailServices.sendEmail(userEmail.get(), generatedCode, "confirmationEmail");
        } else if (userEmail.isPresent() && !userEmail.get().isEnabled() && verificationEmailCodeService.checkIfCodeExpiredOrNot(userEmail.get().getId()).isPresent()) {
            return Response.builder().message("try again after 15 min").build();
        }

        var user = User.builder()
                .firstName(request.getFirstname().replaceAll("\\s", ""))
                .lastName(request.getLastname().replaceAll("\\s", ""))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .longitude(request.getLongitude() == 0 ? 31.2357  : request.getLongitude())
                .latitude(request.getLatitude() == 0 ? 30.0444 : request.getLatitude())
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

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return AuthenticationResponse.builder().message("invalid password, please try again or use forget password to reset password").build();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user.get());
        expireAllTokens(user.get());
        saveToken(user.get(), jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("log in success")
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
        var jwtToken = jwtService.generateToken(user);
        expireAllTokens(user);
        saveToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("activated")
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
    public AuthenticationResponse passwordConfirmationCode(String email, int code) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("user not found"));

        Optional<ResetPasswordCode> resetPasswordCode = resetPasswordService
                .getCode(code, user.getId());

        if (resetPasswordCode.isEmpty()) {
            return AuthenticationResponse.builder().message("code is invalid").build();
        }

        if (resetPasswordCode.get().getExpiredAt().isBefore(LocalDateTime.now()) || resetPasswordCode.get().getConfirmedAt() != null) {
            return AuthenticationResponse.builder().message("code is expired").build();
        }

        resetPasswordService.setExpired(code, user.getId());
        var jwtToken = jwtService.generateToken(user);
        expireAllTokens(user);
        saveToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("code confirmed")
                .build();
    }

    @Transactional
    public Response updateUserPassword(String authHeader, String password) {
        User user = userService.getUser(authHeader);

        String validationMessage = validatingService.validating(null, null, null, password, 0, 0, 0, null, 4);
        if (validationMessage != "validated") {
            return Response.builder().message(validationMessage).build();
        }

        user.setPassword(passwordEncoder.encode(password));
        return Response.builder().message("password updated").build();
    }

    public AuthenticationResponse authorization(AuthorizationRequest request) {

        if (!request.getEmail().contains("@gmail.com")) {
            return AuthenticationResponse.builder().message("Email is not valid").build();
        }

        String validationMessage = validatingService.validating(null, null, null, null, 0, 0, 0, request.getImageURL(), 7);
        if (validationMessage != "validated") {
            return AuthenticationResponse.builder().message(validationMessage).build();
        }

        Optional<User> userEmail = userRepository.findUserByEmail(request.getEmail());

        if (userEmail.isPresent()) {
            var jwtToken = jwtService.generateToken(userEmail.get());
            expireAllTokens(userEmail.get());
            saveToken(userEmail.get(), jwtToken);
            return AuthenticationResponse.builder().token(jwtToken).message("log in successfully").build();
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .imageURL(request.getImageURL())
                .longitude(31.2357)
                .latitude(30.0444)
                .phoneNumber(0)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .enable(true)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveToken(user, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).message("log in successfully").build();
    }
}