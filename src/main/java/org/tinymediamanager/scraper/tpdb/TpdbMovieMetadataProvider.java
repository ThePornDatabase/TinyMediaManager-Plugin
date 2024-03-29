package org.tinymediamanager.scraper.tpdb;

import org.tinymediamanager.core.movie.MovieSearchAndScrapeOptions;
import org.tinymediamanager.scraper.MediaMetadata;
import org.tinymediamanager.scraper.MediaProviderInfo;
import org.tinymediamanager.scraper.MediaSearchResult;
import org.tinymediamanager.scraper.entities.MediaType;
import org.tinymediamanager.scraper.exceptions.NothingFoundException;
import org.tinymediamanager.scraper.exceptions.ScrapeException;
import org.tinymediamanager.scraper.interfaces.IMovieMetadataProvider;
import org.tinymediamanager.scraper.tpdb.entities.SceneEntity;
import org.tinymediamanager.scraper.tpdb.entities.SceneSearch;
import org.tinymediamanager.scraper.tpdb.service.Controller;

import java.util.SortedSet;
import java.util.TreeSet;

public class TpdbMovieMetadataProvider extends TpdbMetadataProvider implements IMovieMetadataProvider {
    @Override
    public MediaProviderInfo createMediaProviderInfo() {
        MediaProviderInfo info = super.createMediaProviderInfo();

        info.getConfig().addText("apiKey", "", true);
        info.getConfig().load();

        return info;
    }

    @Override
    public SortedSet<MediaSearchResult> search(MovieSearchAndScrapeOptions options) throws ScrapeException {
        String apiKey = getProviderInfo().getConfig().getValue("apiKey");
        TpdbApi api = new TpdbApi(apiKey);
        Controller controller = api.getController();

        SortedSet<MediaSearchResult> results = new TreeSet<>();

        String query = options.getSearchQuery();

        SceneSearch search;
        try {
            search = controller.getScenesFromQuery(query);
        } catch (Exception e) {
            throw new ScrapeException(e);
        }

        if (search == null || search.data.isEmpty()) {
            throw new NothingFoundException();
        }

        float score = 100.0F;
        for (SceneEntity scene : search.data) {
            MediaSearchResult data = new MediaSearchResult(getId(), MediaType.MOVIE);

            data.setId(getId(), scene.id);
            data.setTitle(scene.title);

            data.setYear(api.getYear(api.getDate(scene.date)));

            data.setPosterUrl(scene.posters.small);

            data.setScore(score--);
            results.add(data);
        }

        return results;
    }

    @Override
    public MediaMetadata getMetadata(MovieSearchAndScrapeOptions options) throws ScrapeException {
        String apiKey = this.getProviderInfo().getConfig().getValue("apiKey");
        TpdbApi api = new TpdbApi(apiKey);

        SceneEntity scene = null;

        String id = options.getIdAsString(getId());
        if (id != null) {
            try {
                scene = api.getScene(id, TpdbApi.SceneType.SCENE);
            } catch (Exception e) {
                throw new ScrapeException(e);
            }
        }

        return api.setMediaMetadata(scene);
    }
}
