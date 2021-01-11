package com.example.xinbank;

public class customerHelperClass {
    private String DOB, city, email, fullname, ic, phone, state, user_id;
    private char gender;
    private int zip;

    public customerHelperClass() {
    }

    public customerHelperClass(String DOB, String city, String email, String fullname, String ic, String phone, String state, String user_id, char gender, int zip) {
        this.DOB = DOB;
        this.city = city;
        this.email = email;
        this.fullname = fullname;
        this.ic = ic;
        this.phone = phone;
        this.state = state;
        this.user_id = user_id;
        this.gender = gender;
        this.zip = zip;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
}
