package com.example.restuarantms;

import java.sql.Date;
import java.util.List;

/**
 * Base Order class - represents a general order
 */
public class Order {
    protected Integer orderId;
    protected Integer customerId;
    protected List<ProductsData> items;
    protected Double totalAmount;
    protected Date orderDate;
    protected String status;

    // Constructor
    public Order(Integer orderId, Integer customerId, List<ProductsData> items, 
                Double totalAmount, Date orderDate, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Method to calculate total
    public Double calculateTotal() {
        Double total = 0.0;
        if (items != null) {
            for (ProductsData item : items) {
                total += item.getPrice();
            }
        }
        return total;
    }

    // Getters
    public Integer getOrderId() {
        return orderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public List<ProductsData> getItems() {
        return items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }
}
