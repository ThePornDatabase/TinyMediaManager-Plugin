package org.tinymediamanager.scraper.tpdb.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.internal.bind.DateTypeAdapter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.scraper.http.TmmHttpClient;
import org.tinymediamanager.scraper.tpdb.Const;
import org.tinymediamanager.scraper.tpdb.entities.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Date;

public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private final Retrofit retrofit;
    private String apiKey;

    /**
     * setting up the retrofit object with further debugging options if needed
     *
     * @param debug true or false
     */
    public Controller(boolean debug) {
        OkHttpClient.Builder builder = TmmHttpClient.newBuilder();
        if (debug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(LOGGER::debug);
            logging.setLevel(Level.BODY);
            builder.addInterceptor(logging);
        }
        retrofit = buildRetrofitInstance(builder.build());
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private GsonBuilder getGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        // class types
        builder.registerTypeAdapter(Integer.class, (JsonDeserializer<Integer>) (json, typeOfT, context) -> {
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                return 0;
            }
        });
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        return builder;
    }

    /**
     * call the search Info
     *
     * @param searchTerm the movie name
     * @return the {@link SceneSearch} item
     * @throws IOException any exception that could occur
     */
    public SceneSearch getScenesFromQuery(String searchTerm) throws IOException {
        return getService().scenesSearch("Bearer " + apiKey, searchTerm).execute().body();
    }

    /**
     * call the scrape service via ID search
     *
     * @param id the ID to search for
     * @return the {@link SceneGet} item
     * @throws IOException any exception that could occur
     */
    public SceneGet getSceneFromId(String id) throws IOException {
        return getService().sceneScrapeById("Bearer " + apiKey, id).execute().body();
    }

    /**
     * call the search Info
     *
     * @param searchTerm the movie name
     * @return the {@link SceneSearch} item
     * @throws IOException any exception that could occur
     */
    public SceneSearch getMoviesFromQuery(String searchTerm) throws IOException {
        return getService().moviesSearch("Bearer " + apiKey, searchTerm).execute().body();
    }

    /**
     * call the scrape service via ID search
     *
     * @param id the ID to search for
     * @return the {@link SceneGet} item
     * @throws IOException any exception that could occur
     */
    public SceneGet getMovieFromId(String id) throws IOException {
        return getService().movieScrapeById("Bearer " + apiKey, id).execute().body();
    }

    /**
     * call the search Info
     *
     * @param searchTerm the movie name
     * @return the {@link SceneSearch} item
     * @throws IOException any exception that could occur
     */
    public SceneSearch getJAVFromQuery(String searchTerm) throws IOException {
        return getService().javSearch("Bearer " + apiKey, searchTerm).execute().body();
    }

    /**
     * call the scrape service via ID search
     *
     * @param id the ID to search for
     * @return the {@link SceneGet} item
     * @throws IOException any exception that could occur
     */
    public SceneGet getJAVFromId(String id) throws IOException {
        return getService().javScrapeById("Bearer " + apiKey, id).execute().body();
    }

    /**
     * call the scrape service via ID search
     *
     * @param id the ID to search for
     * @return the {@link SiteGet} item
     * @throws IOException any exception that could occur
     */
    public SiteGet getSiteFromId(String id) throws IOException {
        return getService().sitesScrapeById("Bearer " + apiKey, id).execute().body();
    }

    /**
     * Returns the created Retrofit Service
     *
     * @return retrofit object
     */
    private TpdbService getService() {
        return retrofit.create(TpdbService.class);
    }

    /**
     * Builder Class for retrofit Object
     *
     * @param client the http client
     * @return a new retrofit object.
     */
    private Retrofit buildRetrofitInstance(OkHttpClient client) {
        return new Retrofit.Builder().client(client).baseUrl(Const.APIBaseURL)
                .addConverterFactory(GsonConverterFactory.create(getGsonBuilder().create())).build();
    }
}
