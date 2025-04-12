package org.tinymediamanager.scraper.tpdb;

import org.tinymediamanager.scraper.ArtworkSearchAndScrapeOptions;
import org.tinymediamanager.scraper.entities.MediaArtwork;
import org.tinymediamanager.scraper.exceptions.ScrapeException;
import org.tinymediamanager.scraper.interfaces.IMovieArtworkProvider;
import org.tinymediamanager.scraper.tpdb.entities.SceneEntity;

import java.util.ArrayList;
import java.util.List;

public class TpdbMovieArtworkProvider extends TpdbMetadataProvider implements IMovieArtworkProvider {
    @Override
    public List<MediaArtwork> getArtwork(ArtworkSearchAndScrapeOptions options) throws ScrapeException {
        String apiKey = getProviderInfo().getConfig().getValue("apiKey");
        TpdbApi api = new TpdbApi(apiKey);
        TpdbApi.SceneType type = api.getType(getProviderInfo().getConfig().getValue("type"));
        String id = options.getIdAsString(getId());

        List<MediaArtwork> artworks = new ArrayList<>();

        SceneEntity scene;
        try {
            scene = api.getScene(id, type);
        } catch (Exception e) {
            throw new ScrapeException(e);
        }

        MediaArtwork artwork = new MediaArtwork(getId(), MediaArtwork.MediaArtworkType.POSTER);
        artwork.addImageSize(1000, 1500, scene.posters.large, MediaArtwork.PosterSizes.getSizeOrder(1000));
        artwork.setOriginalUrl(scene.posters.large);
        artwork.setPreviewUrl(scene.posters.small);
        artworks.add(artwork);

        artwork = new MediaArtwork(getId(), MediaArtwork.MediaArtworkType.BACKGROUND);
        artwork.addImageSize(1500, 1500, scene.background.large, MediaArtwork.PosterSizes.getSizeOrder(1500));
        artwork.addImageSize(750, 750, scene.background.medium, MediaArtwork.PosterSizes.getSizeOrder(750));
        artwork.addImageSize(500, 500, scene.background.small, MediaArtwork.PosterSizes.getSizeOrder(500));
        artwork.setOriginalUrl(scene.background.full);
        artwork.setPreviewUrl(scene.background.small);
        artworks.add(artwork);

        artwork = new MediaArtwork(getId(), MediaArtwork.MediaArtworkType.THUMB);
        artwork.addImageSize(1500, 1000, scene.background.full, MediaArtwork.PosterSizes.getSizeOrder(1500));
        artwork.setOriginalUrl(scene.background.full);
        artwork.setPreviewUrl(scene.background.small);
        artworks.add(artwork);

        return artworks;
    }
}
