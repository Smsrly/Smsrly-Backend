package com.example.smsrly.service;

import com.example.smsrly.entity.ResetPasswordCode;
import com.example.smsrly.entity.User;
import com.example.smsrly.entity.VerificationEmailCode;
import com.example.smsrly.repository.ResetPasswordCodeRepository;
import com.example.smsrly.response.Response;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServices extends SimpleMailMessage {

    private final VerificationEmailCodeService verificationEmailCodeService;
    private final ResetPasswordCodeRepository resetPasswordCodeRepository;
    private final JavaMailSender mailSender;

    public Response sendEmail(User user, int generatedCode, String type) {

        if (type.equals("confirmationEmail")) {
            VerificationEmailCode verificationEmailCode = new VerificationEmailCode(
                    generatedCode,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user);
            verificationEmailCodeService.saveVerificationCode(verificationEmailCode);
        } else {
            ResetPasswordCode resetPasswordCode = new ResetPasswordCode(
                    generatedCode,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user);
            resetPasswordCodeRepository.save(resetPasswordCode);

        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageTemplate =
                    new MimeMessageHelper(message, "utf-8");
            messageTemplate.setText(confirmationEmailTemplate(user.getFirstName() + " " + user.getLastName(), generatedCode), true);
            messageTemplate.setText(type.equals("confirmationEmail") ?
                    confirmationEmailTemplate(user.getFirstName() + " " + user.getLastName(), generatedCode) :
                    resetPasswordTemplate(user.getFirstName() + " " + user.getLastName(), generatedCode), true);
            messageTemplate.setTo(user.getEmail());
            messageTemplate.setSubject(type.equals("confirmationEmail") ? "Confirm your email" : "Reset your password");
            messageTemplate.setFrom("smsrly2023@gmail.com");
            mailSender.send(message);

        } catch (MessagingException e) {
            return Response.builder().message("failed to send email, " + e).build();
        }
        return Response.builder().message("verification code sent").build();
    }

    private String confirmationEmailTemplate(String userName, int code) {
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

    public String resetPasswordTemplate(String userName, int code) {
        return "<body style=\"background-color: #f6f6f6;\">\n" +
                "    <div\n" +
                "        style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px;\">\n" +
                "        <h1 style=\"color: #3f51b5;\">Reset Password</h1>\n" +
                "        <p style=\"color: #4d4d4d;\">Hello " + userName + ",</p>\n" +
                "        <p style=\"color: #4d4d4d;\">You recently requested to reset your password for your account. Please enter the\n" +
                "            following code in the reset\n" +
                "            password page to proceed:</p>\n" +
                "        <p style=\"font-size: 24px; font-weight: bold; color: #3f51b5;\">" + code + "</p>\n" +
                "        <p style=\"color: #4d4d4d;\">If you did not request a password reset, please ignore this email</p>\n" +
                "        <p style=\"color: #4d4d4d;\">Thanks,</p>\n" +
                "        <p style=\"color: #3f51b5; font-weight: bold;\">Smsrly Team</p>\n" +
                "    </div>\n" +
                "</body>\n";
    }

}
