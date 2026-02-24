package com.example.restuarantms;

/**
 * CatalogItem interface - for items that can be displayed in catalog
 */
public interface CatalogItem {
    String getItemName();
    Double getItemPrice();
    String getItemDescription();
    String getItemImage();
}
