
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Epub implements Serializable
{

    @SerializedName("isAvailable")
    @Expose
    private boolean isAvailable;
    @SerializedName("acsTokenLink")
    @Expose
    private String acsTokenLink;
    private final static long serialVersionUID = 1043015143405990764L;

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getAcsTokenLink() {
        return acsTokenLink;
    }

    public void setAcsTokenLink(String acsTokenLink) {
        this.acsTokenLink = acsTokenLink;
    }

}
