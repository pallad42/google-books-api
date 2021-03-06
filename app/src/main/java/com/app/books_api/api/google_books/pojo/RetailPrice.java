
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetailPrice implements Serializable
{

    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;
    private final static long serialVersionUID = -5598389880017571655L;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
