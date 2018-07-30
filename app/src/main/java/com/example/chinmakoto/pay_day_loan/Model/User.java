package com.example.chinmakoto.pay_day_loan.Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;
    private ArrayList<String> ViewList = new ArrayList<String>();




    public User(){

    }

    public User(String name, String password,List viewList){
        Name = name;
        Password = password;
        IsStaff="false";
        }

    public ArrayList<String> getViewList() {
        return ViewList;
    }

    public void setViewList(ArrayList<String> viewList) {
        ViewList = viewList;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName(){
        return Name;
    }

    public void setName(String name){
        Name = name;
    }

    public String getPassword(){
        return Password;
    }

}

