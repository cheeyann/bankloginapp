package com.example.xinbank;

import androidx.annotation.NonNull;

public class userHelperClass {
    private String signinName, signinImei, signinPhone,signinEmail, signinCard;

    public userHelperClass() {
    }

    public userHelperClass(String signinName, String signinImei, String signinPhone, String signinEmail, String signinCard) {
        this.signinName = signinName;
        this.signinImei = signinImei;
        this.signinPhone = signinPhone;
        this.signinEmail = signinEmail;
        this.signinCard = signinCard;
    }

    public String getSigninName() {
        return signinName;
    }

    public void setSigninName(String signinName) {
        this.signinName = signinName;
    }

    public String getSigninImei() {
        return signinImei;
    }

    public void setSigninImei(String signinImei) {
        this.signinImei = signinImei;
    }

    public String getSigninPhone() {
        return signinPhone;
    }

    public void setSigninPhone(String signinPhone) {
        this.signinPhone = signinPhone;
    }

    public String getSigninEmail() {
        return signinEmail;
    }

    public void setSigninEmail(String signinEmail) {
        this.signinEmail = signinEmail;
    }

    public String getSigninCard() {
        return signinCard;
    }

    public void setSigninCard(String signinCard) {
        this.signinCard = signinCard;
    }
}
