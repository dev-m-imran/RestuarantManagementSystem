package com.example.restuarantms;

/**
 * CustomerData - represents customer transaction data for display in table
 */
public class CustomerData {
    private Integer customerId;
    private String total;
    private String date;
    private String employeeName;

    public CustomerData(Integer customerId, Double total, String date, String employeeName) {
        this.customerId = customerId;
        this.total = String.format("$%.2f", total);
        this.date = date;
        this.employeeName = employeeName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}