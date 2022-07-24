package org.tinymediamanager.scraper.tpdb.entities;

import com.google.gson.annotations.SerializedName;

public class PerformerEntity {
    @SerializedName("id")
    public String id;

    @SerializedName("slug")
    public String slug;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    @SerializedName("parent")
    public PerformerEntity parent;

    @SerializedName("extras")
    public PerformerExtrasEntity extras;
}
