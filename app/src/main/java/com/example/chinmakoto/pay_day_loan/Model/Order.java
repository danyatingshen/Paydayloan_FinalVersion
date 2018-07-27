package com.example.chinmakoto.pay_day_loan.Model;

public class Order {
    private String ProductId;
    private String ProductName;
    private String Price;

    public Order() {
    }

    public Order(String productId, String productName, String price) {
        ProductId = productId;
        ProductName = productName;
        Price = price;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
