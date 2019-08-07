package com.example.derich.bizwiz.products;

public class Products {private String product_name;private String Quantity;String product_price; private int status;

    public Products(String product_name,String Quantity,String product_price, int status) {
        this.product_name = product_name;
        this.Quantity = Quantity;
        this.product_price = product_price;
        this.status = status;
    }
    public String getProduct_name() {
        return product_name;
    }
    public String getQuantity() {
        return Quantity;
    }
    public String getProduct_price() {
        return product_price;
    }
    public int getStatus() {
        return status;
    }
}