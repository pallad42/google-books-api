
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageLinks implements Serializable
{

    @SerializedName("smallThumbnail")
    @Expose
    private String smallThumbnail;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    private final static long serialVersionUID = -3827093065526654232L;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
