package com.example.chinmakoto.pay_day_loan.Model;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String walletaccount;
    private String reward_token;
    private String status;
    private List<Order>foods;

    public Request() {
    }

    public Request(String phone, String name, String walletaccount, String reward_token, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.walletaccount = walletaccount;
        this.reward_token = reward_token;
        this.foods = foods;
        this.status="0";//0:called, 1:run lottery, 2: finished (win or not)
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWalletaccount() {
        return walletaccount;
    }

    public void setWalletaccount(String walletaccount) {
        this.walletaccount = walletaccount;
    }

    public String getReward_token() {
        return reward_token;
    }

    public void setReward_token(String reward_token) {
        this.reward_token = reward_token;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
