package com.example.derich.bizwiz.syncFromServer;

public class UnsyncedDataModel {
    public String TransactionType;
    public String itemsRemaining;

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getItemsRemaining() {
        return itemsRemaining;
    }

    public void setItemsRemaining(String itemsRemaining) {
        this.itemsRemaining = itemsRemaining;
    }
}
