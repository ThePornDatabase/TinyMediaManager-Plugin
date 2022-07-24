package org.tinymediamanager.scraper.tpdb.entities;

import com.google.gson.annotations.SerializedName;

public class ImageEntity {
    @SerializedName("full")
    public String full;

    @SerializedName("large")
    public String large;

    @SerializedName("medium")
    public String medium;

    @SerializedName("small")
    public String small;
}
