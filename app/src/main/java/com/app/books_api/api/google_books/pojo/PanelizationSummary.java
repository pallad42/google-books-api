
package com.app.books_api.api.google_books.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanelizationSummary implements Serializable
{

    @SerializedName("containsEpubBubbles")
    @Expose
    private boolean containsEpubBubbles;
    @SerializedName("containsImageBubbles")
    @Expose
    private boolean containsImageBubbles;
    private final static long serialVersionUID = 4265471745869745942L;

    public boolean isContainsEpubBubbles() {
        return containsEpubBubbles;
    }

    public void setContainsEpubBubbles(boolean containsEpubBubbles) {
        this.containsEpubBubbles = containsEpubBubbles;
    }

    public boolean isContainsImageBubbles() {
        return containsImageBubbles;
    }

    public void setContainsImageBubbles(boolean containsImageBubbles) {
        this.containsImageBubbles = containsImageBubbles;
    }

}
