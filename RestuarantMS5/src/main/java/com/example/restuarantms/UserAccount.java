package com.example.restuarantms;

import java.sql.SQLException;

/**
 * UserAccount interface - for user account management
 */
public interface UserAccount {
    String getUsername();
    String getPassword();
    boolean validateLogin(String username, String password) throws SQLException;
}