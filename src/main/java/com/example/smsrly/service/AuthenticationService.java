package com.example.smsrly.service;

import com.example.smsrly.config.JwtService;
import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.entity.ConfirmationCode;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
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
public class AuthenticationService extends SimpleMailMessage {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationCodeService confirmationCodeService;
    private final JavaMailSender mailSender;

    private String token;
    private int id;


    public String register(RegisterRequest request) {

        int generatedCode = generateCode();
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
        var jwtToken = jwtService.generateToken(user);

        ConfirmationCode confirmationCode = new ConfirmationCode(
                generatedCode,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
        confirmationCodeService.saveVerificationCode(confirmationCode);
        token = jwtToken;
        id = user.getId();

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageTemplate =
                    new MimeMessageHelper(message, "utf-8");
            messageTemplate.setText(emailTemplate(user.getFirstName() +" "+ user.getLastName(), generatedCode), true);
            messageTemplate.setTo(user.getEmail());
            messageTemplate.setSubject("Confirm your email");
            messageTemplate.setFrom("smsrly2023@gmail.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            return "failed to send email, " + e;
        }
        return "verification email sent";
    }

    private int generateCode() {
        Random random = new Random();
        int code = 0 ;
        while (code < 1000) {
            code = random.nextInt(10000);
        }
        return code;
    }


    private String emailTemplate(String userName, int code) {
        return "<body style=\"background-color: #f6f6f6;\">\n" +
                "    <div\n" +
                "        style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px;\">\n" +
                "        <h1 style=\"color: #3f51b5;\">Verification Code</h1>\n" +
                "        <p style=\"color: #4d4d4d;\">Dear " + userName + ",</p>\n" +
                "        <p style=\"color: #4d4d4d;\">Please use the following verification code to verify your email address:</p>\n" +
                "        <p style=\"font-size: 24px; font-weight: bold; color: #3f51b5;\">" + code + "</p>\n" +
                "        <p style=\"color: #4d4d4d;\">This code will expire after 15 minutes. Please use it as soon as possible.</p>\n" +
                "        <p style=\"color: #4d4d4d;\">If you did not request this verification code, please ignore this email.</p>\n" +
                "        <p style=\"color: #4d4d4d;\">Thank you for using our service!</p>\n" +
                "        <p style=\"color: #4d4d4d;\">Best regards,</p>\n" +
                "        <p style=\"color: #3f51b5; font-weight: bold;\">Smsrly Team</p>\n" +
                "    </div>\n" +
                "</body>";
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
    public AuthenticationResponse confirmCode(String code) {
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
        return AuthenticationResponse.builder()
                .token(token)
                .id(id)
                .build();
    }


}