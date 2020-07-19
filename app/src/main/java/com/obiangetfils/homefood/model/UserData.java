package com.obiangetfils.homefood.model;

public class UserData {
    String usernameData, phoneData, emailData, passwordData;

    public UserData(String usernameData, String phoneData, String emailData, String passwordData) {
        this.usernameData = usernameData;
        this.phoneData = phoneData;
        this.emailData = emailData;
        this.passwordData = passwordData;
    }

    public String getUsernameData() {
        return usernameData;
    }

    public void setUsernameData(String usernameData) {
        this.usernameData = usernameData;
    }

    public String getPhoneData() {
        return phoneData;
    }

    public void setPhoneData(String phoneData) {
        this.phoneData = phoneData;
    }

    public String getEmailData() {
        return emailData;
    }

    public void setEmailData(String emailData) {
        this.emailData = emailData;
    }

    public String getPasswordData() {
        return passwordData;
    }

    public void setPasswordData(String passwordData) {
        this.passwordData = passwordData;
    }
}
