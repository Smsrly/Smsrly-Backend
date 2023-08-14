package com.example.smsrly.service;

import com.example.smsrly.exception.InputException;
import com.example.smsrly.utilities.EmailType;
import com.example.smsrly.utilities.Util;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.example.smsrly.utilities.EmailType.CONFIRMATION_EMAIL;

@Service
@RequiredArgsConstructor
public class EmailServices extends SimpleMailMessage {

    private final OTPService otpService;
    private final JavaMailSender mailSender;
    private final Util util;


    public String sendEmail(String userFirstName, String userLastName, String userEmail, EmailType type) throws MessagingException {
        String OTPKey = util.extractUserNameFromEmail(userEmail);

        if (otpService.isOTPCodeExists(OTPKey)) {
            throw new InputException(util.getMessage("otp.time.limitation"));
        }

        Integer OTPCode = otpService.generateOTP(OTPKey);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageTemplate =
                    new MimeMessageHelper(message, "utf-8");
            messageTemplate.setText(type.equals(CONFIRMATION_EMAIL) ?
                    confirmationEmailTemplate(userFirstName + " " + userLastName, OTPCode) :
                    resetPasswordTemplate(userFirstName + " " + userLastName, OTPCode), true);
            messageTemplate.setTo(userEmail);
            messageTemplate.setSubject(type.equals(CONFIRMATION_EMAIL) ? "Confirm your email" : "Reset your password");
            messageTemplate.setFrom("smsrly2023@gmail.com");
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new MessagingException("failed to send email, " + e);
        }
        return util.getMessage("verification.sent");
    }

    private String confirmationEmailTemplate(String userName, int code) {
        return "<body style=\"background-color: #f6f6f6;\">\n" +
                "    <div\n" +
                "        style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px;\">\n" +
                "        <h1 style=\"color: #3f51b5;\">Verification Code</h1>\n" +
                "        <p style=\"color: #4d4d4d;\">Dear " + userName + ",</p>\n" +
                "        <p style=\"color: #4d4d4d;\">Please use the following verification code to verify your email address:</p>\n" +
                "        <p style=\"font-size: 24px; font-weight: bold; color: #3f51b5;\">" + code + "</p>\n" +
                "        <p style=\"color: #4d4d4d;\">This code will expire after 10 minutes. Please use it as soon as possible.</p>\n" +
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
