
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListPrice_ implements Serializable
{

    @SerializedName("amountInMicros")
    @Expose
    private long amountInMicros;
    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;
    private final static long serialVersionUID = 5595335740352406651L;

    public long getAmountInMicros() {
        return amountInMicros;
    }

    public void setAmountInMicros(long amountInMicros) {
        this.amountInMicros = amountInMicros;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
