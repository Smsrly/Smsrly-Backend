package com.example.smsrly.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidatingService {

    private boolean isValidEmail(String email) {

        // Email should match the regular expression for a valid email
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean isValidPassword(String password) {

        // Password length should be between 8 and 20 characters
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // Password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^])[A-Za-z\\d@$!%*?&#^]{8,20}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {

        // Phone number should match the regular expression for a valid phone number
        Pattern pattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    private boolean isValidLocation(double latitude, double longitude) {

        // Latitude should be between -90 and 90
        if (latitude < -90 || latitude > 90) {
            return false;
        }

        // Longitude should be between -180 and 180
        if (longitude < -180 || longitude > 180) {
            return false;
        }

        return true;
    }

    private boolean isValidName(String name, boolean isFirstName) {

        // Name should contain only alphabets and be of appropriate length depending on whether it is first name or last name
        Pattern pattern;

        if (isFirstName) {
            pattern = Pattern.compile("^[\\p{L}\\s]{2,20}$"); // First name should be between 2 and 20 characters long
        } else {
            pattern = Pattern.compile("^[\\p{L}\\s]{2,30}$"); // Last name should be between 2 and 30 characters long
        }

        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    private boolean isValidURL(String url) {

        // URL should match the regular expression for a valid URL
        Pattern pattern = Pattern.compile("(http|https)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\S*)");
        Matcher matcher = pattern.matcher(url);

        return matcher.matches();
    }

    protected String validating(String firstName, String lastName, String email, String password, long phoneNumber, double latitude, double longitude, String imageURL, int validatingNumber) {


        if (validatingNumber == 1 || validatingNumber == 0) {
            if (!isValidName(firstName.replaceAll("\\s", ""), true)) return "First name is valid";
        }

        if (validatingNumber == 2 || validatingNumber == 0) {
            if (!isValidName(lastName.replaceAll("\\s", ""), false)) return "Last name is valid";
        }

        if (validatingNumber == 3 || validatingNumber == 0) {
            if (!isValidEmail(email)) return "Email is not valid";
        }

        if (validatingNumber == 4 || validatingNumber == 0) {
            if (!isValidPassword(password))
                return "Password is not valid, password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character";
        }

        if (validatingNumber == 5 || validatingNumber == 0) {
            if (!isValidPhoneNumber("+" + phoneNumber)) return "Phone number is not valid";
        }

        if (validatingNumber == 6 || validatingNumber == 0) {
            if (!isValidLocation(latitude, longitude)) return "Location is not valid";
        }

        if (validatingNumber == 7 && imageURL != null) {
            if (!isValidURL(imageURL)) return "Image URL is not valid";
        }

        return "validated";
    }


    private boolean isValidRealEstateText(String text, boolean isTitle) {

        // Name should contain only alphabets and be of appropriate length depending on whether it is first name or last name
        Pattern pattern;

        if (isTitle) {
            pattern = Pattern.compile("^[\\p{L}\\s]{10,20}$"); // title name should be between 10 and 20 characters long
        } else {
            pattern = Pattern.compile("^[\\p{L}\\s]{20,150}$"); // des. name should be between 20 and 150 characters long
        }

        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static boolean isValidCity(String city) {
        // Regular expression to match only alphabetical characters and spaces
        String regex = "^[a-zA-Z ]*$";
        return Pattern.matches(regex, city);
    }

    public static boolean isValidCountry(String country) {
        // Regular expression to match only alphabetical characters and spaces
        String regex = "^[a-zA-Z ]*$";
        return Pattern.matches(regex, country);
    }

    protected String validatingRealEstate(String title, String description, double area, int floorNumber, int bathroomNumber, int roomNumber, double price, String city, String country, int validatingNumber) {

        if (validatingNumber == 1 || validatingNumber == 0) {
            if (!isValidRealEstateText(title, true)) return "this is invalid title";
        }

        if (validatingNumber == 2 || validatingNumber == 0) {
            if (!isValidRealEstateText(description, false)) return "this is invalid description";
        }

        if (validatingNumber == 3 || validatingNumber == 0) {
            if (area < 1 || area > 1000000) return "this is invalid area";
        }

        if (validatingNumber == 4 || validatingNumber == 0) {
            if (floorNumber < 0 || floorNumber > 163) return "this is invalid floor number";
        }

        if (validatingNumber == 5 || validatingNumber == 0) {
            if (bathroomNumber < 1 || bathroomNumber > 10) return "this is invalid bathroom number";
        }

        if (validatingNumber == 6 || validatingNumber == 0) {
            if (roomNumber < 1 || roomNumber > 20) return "this is invalid room number";
        }

        if (validatingNumber == 7 || validatingNumber == 0) {
            if (price < 1000 || price > 1000000000) return "this is invalid price";
        }

        if (validatingNumber == 8 || validatingNumber == 0) {
            if (!isValidCity(city)) return "this is invalid city";
            if (!isValidCountry(country)) return "this is invalid country";
        }
        return "validated";
    }

}
