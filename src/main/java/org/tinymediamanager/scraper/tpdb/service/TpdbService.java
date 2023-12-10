package org.tinymediamanager.scraper.tpdb.service;

import org.tinymediamanager.scraper.tpdb.entities.*;
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
    Call<SceneGet> sceneScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

    @Headers({
            "User-Agent: " + UserAgent,
    })
    @GET("/movies")
    Call<SceneSearch> moviesSearch(@Header("Authorization") String apikey, @Query("parse") String query);

    @Headers({
        "User-Agent: " + UserAgent,
    })
    @GET("/movies/{id}")
    Call<SceneGet> movieScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

    @Headers({
            "User-Agent: " + UserAgent,
    })
    @GET("/jav")
    Call<SceneSearch> javSearch(@Header("Authorization") String apikey, @Query("parse") String query);

    @Headers({
        "User-Agent: " + UserAgent,
    })
    @GET("/jav/{id}")
    Call<SceneGet> javScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

    @Headers({
        "User-Agent: " + UserAgent,
    })
    @GET("/sites/{id}")
    Call<SiteGet> sitesScrapeById(@Header("Authorization") String apikey, @Path("id") String id);

}
