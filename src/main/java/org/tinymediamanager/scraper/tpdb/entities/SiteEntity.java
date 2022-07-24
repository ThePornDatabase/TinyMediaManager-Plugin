package org.tinymediamanager.scraper.tpdb.entities;

import com.google.gson.annotations.SerializedName;

public class SiteEntity {
    @SerializedName("id")
    public Integer id;

    @SerializedName("parent_id")
    public Integer parent_id;

    @SerializedName("network_id")
    public Integer network_id;

    @SerializedName("name")
    public String name;
}
