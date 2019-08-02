package com.example.derich.bizwiz.clients;

public class Clients {private String client_fullName;private String client_debt;private String Number;String client_Email;private int status;

    public Clients(String client_fullName,String client_debt,String Number,String client_Email, int status) {
        this.client_fullName = client_fullName;
        this.client_debt = client_debt;
        this.Number = Number;
        this.client_Email = client_Email;
        this.status = status;
    }

    public String getClient_fullName() {
        return client_fullName;
    }
    public String getClient_debt() {
        return client_debt;
    }
    public String getClient_number() {
        return Number;
    }
    public String getClient_Email() {
        return client_Email;
    }

    public int getStatus() {
        return status;
    }
}