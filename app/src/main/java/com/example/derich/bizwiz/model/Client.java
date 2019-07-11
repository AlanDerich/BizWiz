package com.example.derich.bizwiz.model;

public class Client {

    private int clientId;
    private String firstName;
    private String secondName;
    private String clientEmail;
    private String clientDebt;
    private String Number;

    public int getId() {
        return clientId;
    }

    public void setId(int clientId){
        this.clientId = clientId;
    }

    public String getfirstName(){
        return firstName;
    }

    public void setfirstName(String firstName){
        this.firstName = firstName;
    }
    public String getsecondName(){
        return secondName;
    }

    public void setsecondName(String secondName) { this.secondName = secondName; }

    public String getClientEmail(){
        return clientEmail;
    }

    public void setclientEmail(String clientEmail){
        this.clientEmail = clientEmail;
    }
    public String getClientDebt(){
        return clientDebt;
    }

    public void setclientDebt(String clientDebt){
        this.clientDebt = clientDebt;
    }

    public String getNumber(){
        return Number;
    }

    public void setNumber(String Number){
        this.Number = Number;
    }

}
