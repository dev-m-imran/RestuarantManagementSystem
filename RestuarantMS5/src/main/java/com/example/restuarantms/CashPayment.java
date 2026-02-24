package com.example.restuarantms;

/**
 * CashPayment extends Payment - represents cash payment
 */
public class CashPayment extends Payment {
    private Double cashReceived;
    private Double change;

    // Constructor
    public CashPayment(Double amount, String paymentDate, String status, 
                      Double cashReceived, Double change) {
        super(amount, paymentDate, status);
        this.cashReceived = cashReceived;
        this.change = change;
    }

    // Implementation of abstract method
    @Override
    public String processPayment() {
        if (cashReceived >= amount) {
            change = cashReceived - amount;
            status = "Completed";
            return "Cash payment processed. Change: £" + change;
        } else {
            status = "Failed";
            return "Insufficient cash. Required: £" + amount;
        }
    }

    // Getters
    public Double getCashReceived() {
        return cashReceived;
    }

    public Double getChange() {
        return change;
    }
}
