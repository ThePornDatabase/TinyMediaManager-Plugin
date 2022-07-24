package org.tinymediamanager.scraper.tpdb.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SceneEntity {
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("date")
    public String date;

    @SerializedName("url")
    public String url;

    @SerializedName("posters")
    public ImageEntity posters;

    @SerializedName("background")
    public ImageEntity background;

    @SerializedName("trailer")
    public String trailer;

    @SerializedName("performers")
    public List<PerformerEntity> performers;

    @SerializedName("site")
    public SiteEntity site;

    @SerializedName("tags")
    public List<TagEnity> tags;
}
