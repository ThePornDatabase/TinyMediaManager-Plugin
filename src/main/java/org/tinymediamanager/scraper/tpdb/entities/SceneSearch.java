package org.tinymediamanager.scraper.tpdb.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SceneSearch {
    @SerializedName("data")
    public List<SceneEntity> data = null;
}
