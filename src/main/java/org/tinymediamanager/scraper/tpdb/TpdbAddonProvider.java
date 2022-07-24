package org.tinymediamanager.scraper.tpdb;

import org.tinymediamanager.scraper.interfaces.IMediaProvider;
import org.tinymediamanager.scraper.spi.IAddonProvider;

import java.util.ArrayList;
import java.util.List;

public class TpdbAddonProvider implements IAddonProvider {

    @Override
    public List<Class<? extends IMediaProvider>> getAddonClasses() {
        List<Class<? extends IMediaProvider>> addons = new ArrayList<>();

        addons.add(TpdbMovieMetadataProvider.class);
        addons.add(TpdbMovieArtworkProvider.class);
        addons.add(TpdbMovieTrailerProvider.class);

        return addons;
    }
}
