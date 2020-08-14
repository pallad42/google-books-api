
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchInfo implements Serializable
{

    @SerializedName("textSnippet")
    @Expose
    private String textSnippet;
    private final static long serialVersionUID = -9219202428206926219L;

    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }

}
