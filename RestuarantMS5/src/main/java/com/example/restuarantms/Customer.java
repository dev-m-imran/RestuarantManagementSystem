package com.example.restuarantms;

import java.sql.Date;

/**
 * Customer class represents a customer in the restaurant system
 */
public class Customer {
    private Integer id;
    private Integer customerId;
    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Date date;
    private String employeeUsername;
    private Integer paid;

    // Constructor
    public Customer(Integer id, Integer customerId, String productId, String productName, 
                   Integer quantity, Double price, Date date, String employeeUsername, Integer paid) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.employeeUsername = employeeUsername;
        this.paid = paid;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public Integer getPaid() {
        return paid;
    }
}
