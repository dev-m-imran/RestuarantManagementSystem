package com.example.restuarantms;

import java.sql.Date;
import java.util.List;

/**
 * SalesOrder extends Order - represents a sales order in the restaurant
 */
public class SalesOrder extends Order {
    private String employeeUsername;
    private Double amountPaid;
    private Double change;

    // Constructor
    public SalesOrder(Integer orderId, Integer customerId, List<ProductsData> items, 
                     Double totalAmount, Date orderDate, String status,
                     String employeeUsername, Double amountPaid, Double change) {
        super(orderId, customerId, items, totalAmount, orderDate, status);
        this.employeeUsername = employeeUsername;
        this.amountPaid = amountPaid;
        this.change = change;
    }

    // Method to process payment
    public void processPayment(Double amount) {
        this.amountPaid = amount;
        this.change = amount - this.totalAmount;
        this.status = "Paid";
    }

    // Getters
    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public Double getChange() {
        return change;
    }
}
