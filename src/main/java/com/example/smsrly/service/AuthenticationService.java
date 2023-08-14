package com.example.smsrly.service;

import com.example.smsrly.auth.*;
import com.example.smsrly.entity.User;
import com.example.smsrly.exception.InputException;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.utilities.EmailType;
import com.example.smsrly.utilities.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

import static com.example.smsrly.utilities.EmailType.CONFIRMATION_EMAIL;
import static com.example.smsrly.utilities.EmailType.RESET_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final EmailServices emailServices;
    private final TokenService tokenService;
    private final OTPService otpService;
    private final Util util;
    private final UserRepository userRepository;

    public AuthenticationResponse signup(RegistrationRequest request, Integer otp) {

        if (otp < 1000 || otp > 9999) {
            throw new InputException(util.getMessage("otp.validation"));
        }

        String OTPKey = util.extractUserNameFromEmail(request.getEmail());

        if (userService.isUserExists(request.getEmail())) {
            throw new InputException(util.getMessage("account.exists"));
        }

        if (!otpService.isValidateOTP(OTPKey, otp)) {
            throw new InputException(util.getMessage("otp.invalid"));
        }

        User user = User.builder()
                .firstName(request.getFirstName().replaceAll("\\s", ""))
                .lastName(request.getLastName().replaceAll("\\s", ""))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .build();
        return registration(user);
    }

    public Response verifyEmail(OTPRequest request) throws MessagingException {
        return Response.builder().message(userService.isUserExists(request.getEmail()) ? util.getMessage("account.verified") : sendOTP(request, CONFIRMATION_EMAIL)).build();
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        User user = userService.getUserByEmail(authenticationRequest.getEmail());
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new InputException(util.getMessage("account.password.incorrect"));
        }

        return authentication(user, authenticationRequest.getPassword());
    }

    public AuthenticationResponse authorization(AuthorizationRequest request) {
        if (userService.isUserExists(request.getEmail())) {
            User user = userService.getUserByEmail(request.getEmail());
            return authentication(user, null);
        } else {

            if (request.getImageURL() != null && !util.isValidURL(request.getImageURL())) {
                throw new InputException(util.getMessage("account.image.url.invalid"));
            }

            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .imageURL(request.getImageURL())
                    .longitude(null)
                    .latitude(null)
                    .phoneNumber(null)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();
            return registration(user);
        }
    }

    public Response resetPassword(String email) throws MessagingException {
        User user = userService.getUserByEmail(email);

        return Response.builder()
                .message(
                        sendOTP(
                                OTPRequest.builder()
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .email(user.getEmail())
                                        .build(),
                                RESET_PASSWORD
                        )
                ).build();
    }

    public AuthenticationResponse passwordConfirmationCode(String email, int code) {
        User user = userService.getUserByEmail(email);
        String OTPKey = util.extractUserNameFromEmail(email);

        if (!otpService.isValidateOTP(OTPKey, code)) {
            throw new InputException(util.getMessage("otp.invalid"));
        }

        return tokenService.generateTokens(user);
    }

    @Transactional
    public Response updateUserPassword(String authHeader, String password) {
        User user = userService.getUser(authHeader);
        if (!util.isValidPassword(password)) {
            throw new InputException(util.getMessage("account.password.weak"));
        }
        user.setPassword(passwordEncoder.encode(password));
        return Response.builder().message(util.getMessage("account.password.updated")).build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InputException(util.getMessage("token.invalid"));
        }

        refreshToken = jwtService.extractToken(authHeader);

        if (tokenService.isAccessTokenExists(refreshToken)) {
            throw new InputException(util.getMessage("token.access"));
        }

        userEmail = jwtService.extractEmail(refreshToken);

        if (userEmail != null) {
            User user = userService.getUserByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var authResponse = tokenService.generateAccessToken(user, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            } else {
                throw new InputException(util.getMessage("token.invalid"));
            }
        } else {
            throw new InputException(util.getMessage("token.user.not.exists"));
        }
    }

    private String sendOTP(OTPRequest request, EmailType type) throws MessagingException {
        return emailServices.sendEmail(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                type
        );
    }

    private AuthenticationResponse registration(User user) {
        userRepository.save(user);
        return tokenService.generateTokens(user);
    }

    private AuthenticationResponse authentication(User user, String userPassword) {
        if (userPassword != null) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            userPassword
                    )
            );
        }
        return tokenService.generateTokens(user);
    }
}