package com.example.restuarantms;

import java.sql.Date;

/**
 * ProductsData class - represents a product in the restaurant system
 * Implements CatalogItem interface for catalog display
 */
public class ProductsData implements CatalogItem {

    private Integer id;
    private String productId;
    private String productName;
    private String type;
    private Integer stock;
    private Double price;
    private String status;
    private String image;
    private Date date;
    private Integer quantity;

    // ✅ CONSTRUCTOR
    public ProductsData(Integer id, String productId,
                        String productName, String type,
                        Integer stock, Double price,
                        String status, String image, Date date) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.image = image;
        this.date = date;
    }

    public ProductsData(Integer id, String productId,
                        String productName, String type, Integer quantity, Double price,
                        String image){

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.image = image;

    }


    public ProductsData(Integer id, String productName, int quantity, double price) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }


    // ✅ GETTERS (TableView uses these)
    public Integer getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public Integer getQuantity(){
        return quantity;
    }

    // Implementation of CatalogItem interface methods
    @Override
    public String getItemName() {
        return productName;
    }

    @Override
    public Double getItemPrice() {
        return price;
    }

    @Override
    public String getItemDescription() {
        return type + " - " + status;
    }

    @Override
    public String getItemImage() {
        return image;
    }
}
