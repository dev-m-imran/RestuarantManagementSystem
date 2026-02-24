package com.example.restuarantms;

import java.sql.Date;

/**
 * Employee class represents an employee in the restaurant system
 */
public class Employee {
    private Integer id;
    private String username;
    private String password;
    private String question;
    private String answer;
    private Date date;

    // Constructor
    public Employee(Integer id, String username, String password, String question, String answer, Date date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.question = question;
        this.answer = answer;
        this.date = date;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Date getDate() {
        return date;
    }
}
