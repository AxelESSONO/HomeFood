package com.obiangetfils.homefood.model;

public class CreditCardObject {

    private String cardNumber;
    private String validity;
    private String memberName;
    private String cvvCard;
    private int image;

    public CreditCardObject(String cardNumber, String validity, String memberName, String cvvCard, int image) {
        this.cardNumber = cardNumber;
        this.validity = validity;
        this.memberName = memberName;
        this.cvvCard = cvvCard;
        this.image = image;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCvvCard() {
        return cvvCard;
    }

    public void setCvvCard(String cvvCard) {
        this.cvvCard = cvvCard;
    }
}
