package com.example.restuarantms.util;

/**
 * ValidationUtil - utility class for input validation
 */
public class ValidationUtil {

    // Check if string is empty or null
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Check if number is positive
    public static boolean isPositive(Double number) {
        return number != null && number > 0;
    }

    // Check if number is positive integer
    public static boolean isPositiveInteger(Integer number) {
        return number != null && number > 0;
    }

    // Validate email format (simple check)
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    // Validate password length
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
