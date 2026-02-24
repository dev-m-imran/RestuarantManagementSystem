package com.example.restuarantms;

/**
 * Abstract Payment class - base class for different payment types
 */
public abstract class Payment {
    protected Double amount;
    protected String paymentDate;
    protected String status;

    // Constructor
    public Payment(Double amount, String paymentDate, String status) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // Abstract method that must be implemented by subclasses
    public abstract String processPayment();

    // Common method
    public String getPaymentDetails() {
        return "Amount: £" + amount + ", Status: " + status;
    }

    // Getters
    public Double getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }
}
