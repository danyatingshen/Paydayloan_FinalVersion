package com.example.chinmakoto.pay_day_loan.Model;

import java.sql.Time;

public class Food {
    private String Name,Image,Description,Price,Discount,MenuID,Tell,Time,Bid;

    public Food() {
    }

    public Food(String name, String image, String description, String price, String discount, String menuID,String tell,String time,String bid) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuID = menuID;
        Tell=tell;
        Time=time;
        Bid=bid;
    }

    public String getBid() {
        return Bid;
    }

    public void setBid(String bid) {
        Bid = bid;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTell(){
        return Tell;
    }
    public void setTell(String tell){
        Tell=tell;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }
}
