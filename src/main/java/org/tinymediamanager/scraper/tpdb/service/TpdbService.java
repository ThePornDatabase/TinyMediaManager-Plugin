package org.tinymediamanager.scraper.tpdb.service;

import org.tinymediamanager.scraper.tpdb.entities.SceneGet;
import org.tinymediamanager.scraper.tpdb.entities.SceneSearch;
import org.tinymediamanager.scraper.tpdb.entities.SiteGet;
import retrofit2.Call;
import retrofit2.http.*;

import static org.tinymediamanager.scraper.tpdb.Const.UserAgent;

public interface TpdbService {

    @Headers({
        "User-Agent: " + UserAgent,
    })
    @GET("/scenes")
    Call<SceneSearch> scenesSearch(@Header("Authorization") String apikey, @Query("parse") String query);

    @Headers({
        "User-Agent: " + UserAgent,
    })
    @GET("/scenes/{id}")
    Call<SceneGet> scenesScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

    @Headers({
        "User-Agent: " + UserAgent,
    })
    @GET("/sites/{id}")
    Call<SiteGet> sitesScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

}
