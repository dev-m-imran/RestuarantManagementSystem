package com.example.restuarantms.util;

/**
 * FormatUtil - utility class for formatting data
 */
public class FormatUtil {

    // Format currency
    public static String formatCurrency(Double amount) {
        return String.format("£%.2f", amount);
    }

    // Format date
    public static String formatDate(java.util.Date date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    // Format date with time
    public static String formatDateTime(java.util.Date date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }
}
