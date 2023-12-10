package org.tinymediamanager.scraper.tpdb;

import org.tinymediamanager.core.entities.MediaTrailer;
import org.tinymediamanager.scraper.MediaProviderInfo;
import org.tinymediamanager.scraper.TrailerSearchAndScrapeOptions;
import org.tinymediamanager.scraper.exceptions.ScrapeException;
import org.tinymediamanager.scraper.interfaces.IMovieTrailerProvider;
import org.tinymediamanager.scraper.tpdb.entities.SceneEntity;

import java.util.ArrayList;
import java.util.List;

public class TpdbMovieTrailerProvider extends TpdbMetadataProvider implements IMovieTrailerProvider {
    @Override
    public MediaProviderInfo createMediaProviderInfo() {
        MediaProviderInfo info = super.createMediaProviderInfo();

        info.getConfig().addText("apiKey", "", true);
        info.getConfig().load();

        return info;
    }

    @Override
    public List<MediaTrailer> getTrailers(TrailerSearchAndScrapeOptions options) throws ScrapeException {
        String apiKey = getProviderInfo().getConfig().getValue("apiKey");
        TpdbApi api = new TpdbApi(apiKey);
        String id = options.getIdAsString(getId());

        SceneEntity scene;
        try {
            scene = api.getScene(id, TpdbApi.SceneType.SCENE);
        } catch (Exception e) {
            throw new ScrapeException(e);
        }

        List<MediaTrailer> trailers = new ArrayList<>();

        MediaTrailer trailer = new MediaTrailer();
        trailer.setName(scene.title);
        trailer.setProvider(scene.site.name);
        trailer.setUrl(scene.trailer);
        trailers.add(trailer);

        return trailers;
    }
}
