package com.example.restuarantms;

/**
 * Persistable interface - for objects that can be saved to database
 */
public interface Persistable {
    String toInsertQuery();
    String toUpdateQuery();
    String toDeleteQuery();
}
