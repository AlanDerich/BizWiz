package com.example.derich.bizwiz.products;

public class Products {private String product_name;private String Quantity;String product_buying_price;String product_retail_price;String product_wholesale_price; private int status;

    public Products(String product_name,String Quantity,String product_buying_price,String product_retail_price,String product_wholesale_price, int status) {
        this.product_name = product_name;
        this.Quantity = Quantity;
        this.product_buying_price = product_buying_price;
        this.product_retail_price = product_retail_price;
        this.product_wholesale_price = product_wholesale_price;
        this.status = status;
    }
    public String getProduct_name() {
        return product_name;
    }
    public String getQuantity() {
        return Quantity;
    }
    public String getProduct_buying_price() {
        return product_buying_price;
    }
    public String getProduct_retail_price() {
        return product_retail_price;
    }
    public String getProduct_wholesale_price() {
        return product_wholesale_price;
    }
    public int getStatus() {
        return status;
    }
}