package com.example.chinmakoto.pay_day_loan.Model;

public class Lottery {
    private String WalletAccount;

    public Lottery() {
    }

    public Lottery(String walletAccount) {
        this.WalletAccount = walletAccount;
    }

    public String getWalletAccount() {
        return WalletAccount;
    }

    public void setWalletAccount(String walletAccount) {
        WalletAccount = walletAccount;
    }
}


