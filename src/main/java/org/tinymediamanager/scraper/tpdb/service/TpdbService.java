package org.tinymediamanager.scraper.tpdb.service;

import org.tinymediamanager.scraper.tpdb.entities.SceneGet;
import org.tinymediamanager.scraper.tpdb.entities.SceneSearch;
import org.tinymediamanager.scraper.tpdb.entities.SiteGet;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TpdbService {

    @GET("/scenes")
    Call<SceneSearch> scenesSearch(@Header("Authorization") String apikey, @Query("parse") String query);

    @GET("/scenes/{id}")
    Call<SceneGet> scenesScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

    @GET("/sites/{id}")
    Call<SiteGet> sitesScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

}
