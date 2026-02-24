package com.example.restuarantms;

/**
 * CreditCardPayment extends Payment - represents credit card payment
 */
public class CreditCardPayment extends Payment {
    private String cardNumber;
    private String cardHolderName;

    // Constructor
    public CreditCardPayment(Double amount, String paymentDate, String status,
                           String cardNumber, String cardHolderName) {
        super(amount, paymentDate, status);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    // Implementation of abstract method
    @Override
    public String processPayment() {
        if (cardNumber != null && !cardNumber.isEmpty()) {
            status = "Completed";
            return "Credit card payment processed for " + cardHolderName;
        } else {
            status = "Failed";
            return "Invalid card number";
        }
    }

    // Getters
    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }
}
