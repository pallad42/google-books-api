
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndustryIdentifier implements Serializable
{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    private final static long serialVersionUID = 5963628640627892081L;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
