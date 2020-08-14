
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReadingModes implements Serializable
{

    @SerializedName("text")
    @Expose
    private boolean text;
    @SerializedName("image")
    @Expose
    private boolean image;
    private final static long serialVersionUID = -2866152608138877747L;

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

}
