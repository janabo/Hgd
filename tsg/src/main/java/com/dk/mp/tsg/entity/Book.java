package com.dk.mp.tsg.entity;

/**
 * 作者：janabo on 2017/10/25 13:42
 */
public class Book {
    private String id;
    private String name;
    private String stock;//库存

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
