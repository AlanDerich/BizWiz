package com.example.derich.bizwiz.sales;

public class SummaryModel {
    public String productName;
    public int quantitySold;
    public float retailSales;
    public float wholesaleSales;
    public int added;

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public float getRetailSales() {
        return retailSales;
    }

    public void setRetailSales(float retailSales) {
        this.retailSales = retailSales;
    }

    public float getWholesaleSales() {
        return wholesaleSales;
    }

    public void setWholesaleSales(float wholesaleSales) {
        this.wholesaleSales = wholesaleSales;
    }

    public float getExpectedCash() {
        return expectedCash;
    }

    public void setExpectedCash(float expectedCash) {
        this.expectedCash = expectedCash;
    }

    public float expectedCash;
}
