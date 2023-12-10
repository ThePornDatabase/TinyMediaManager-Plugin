package org.tinymediamanager.scraper.tpdb;

import org.tinymediamanager.scraper.ArtworkSearchAndScrapeOptions;
import org.tinymediamanager.scraper.MediaProviderInfo;
import org.tinymediamanager.scraper.entities.MediaArtwork;
import org.tinymediamanager.scraper.exceptions.ScrapeException;
import org.tinymediamanager.scraper.interfaces.IMovieArtworkProvider;
import org.tinymediamanager.scraper.tpdb.entities.SceneEntity;

import java.util.ArrayList;
import java.util.List;

public class TpdbMovieArtworkProvider extends TpdbMetadataProvider implements IMovieArtworkProvider {
    @Override
    public MediaProviderInfo createMediaProviderInfo() {
        MediaProviderInfo info = super.createMediaProviderInfo();

        info.getConfig().addText("apiKey", "", true);
        info.getConfig().load();

        return info;
    }

    @Override
    public List<MediaArtwork> getArtwork(ArtworkSearchAndScrapeOptions options) throws ScrapeException {
        String apiKey = getProviderInfo().getConfig().getValue("apiKey");
        TpdbApi api = new TpdbApi(apiKey);
        String id = options.getIdAsString(getId());

        List<MediaArtwork> artworks = new ArrayList<>();

        SceneEntity scene;
        try {
            scene = api.getScene(id, TpdbApi.SceneType.SCENE);
        } catch (Exception e) {
            throw new ScrapeException(e);
        }

        MediaArtwork artwork = new MediaArtwork(getId(), MediaArtwork.MediaArtworkType.POSTER);
        artwork.setDefaultUrl(scene.posters.large);
        artwork.setPreviewUrl(scene.posters.small);
        artworks.add(artwork);

        artwork = new MediaArtwork(getId(), MediaArtwork.MediaArtworkType.BACKGROUND);
        artwork.setDefaultUrl(scene.background.large);
        artwork.setPreviewUrl(scene.background.small);
        artworks.add(artwork);

        artwork = new MediaArtwork(getId(), MediaArtwork.MediaArtworkType.THUMB);
        artwork.setDefaultUrl(scene.background.full);
        artwork.setPreviewUrl(scene.background.small);
        artworks.add(artwork);

        return artworks;
    }
}
