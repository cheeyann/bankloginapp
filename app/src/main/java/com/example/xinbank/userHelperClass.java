package com.example.xinbank;

import androidx.annotation.NonNull;

import java.util.Date;

public class userHelperClass {

    private String username;
    private String password;
    private String IMEI;
    private Long timestamp;
    private Boolean is_login;
    private String card_num;
    private String onlinecustomer_id;

    public userHelperClass() {
    }

    public userHelperClass(String username, String password, String IMEI, Long timestamp, Boolean is_login, String card_num, String onlinecustomer_id) {
        this.username = username;
        this.password = password;
        this.IMEI = IMEI;
        this.timestamp = timestamp;
        this.is_login = is_login;
        this.card_num = card_num;
        this.onlinecustomer_id = onlinecustomer_id;
    }

    private void generateTimestamp() {
        Date date = new Date();
        timestamp = date.getTime();
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        generateTimestamp();
    }

    public Boolean getIs_login() {
        return is_login;
    }

    public void setIs_login(Boolean is_login) {
        this.is_login = is_login;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getOnlinecustomer_id() {
        return onlinecustomer_id;
    }

    public void setOnlinecustomer_id(String onlinecustomer_id) {
        this.onlinecustomer_id = onlinecustomer_id;
    }
}
