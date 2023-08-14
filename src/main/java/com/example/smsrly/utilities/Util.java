package com.example.smsrly.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class Util {

    private final MessageSource messageSource;

    public String extractUserNameFromEmail(String email) {
        String userName = "";
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') return userName;
            userName += email.charAt(i);
        }
        return userName;
    }

    public boolean isValidPassword(String password) {

        // Password length should be greater than 8 characters
        if (password.length() < 8) {
            return false;
        }

        // Password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^_])[A-Za-z\\d@$!%*?&#^_]+$");
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public boolean isValidURL(String url) {

        // URL should match the regular expression for a valid URL
        Pattern pattern = Pattern.compile("(http|https)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\S*)");
        Matcher matcher = pattern.matcher(url);

        return matcher.matches();
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

}
