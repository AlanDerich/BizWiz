package com.example.derich.bizwiz.sales;

public class ReportModel {
   public String ProductName;
   public int soldItems;
   public int addedItems;
   public float expectedCash;
   public int remainingItems;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(int soldItems) {
        this.soldItems = soldItems;
    }

    public int getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(int addedItems) {
        this.addedItems = addedItems;
    }

    public float getExpectedCash() {
        return expectedCash;
    }

    public void setExpectedCash(Float expectedCash) {
        this.expectedCash = expectedCash;
    }

    public int getRemainingItems() {
        return remainingItems;
    }

    public void setRemainingItems(int remainingItems) {
        this.remainingItems = remainingItems;
    }
}
