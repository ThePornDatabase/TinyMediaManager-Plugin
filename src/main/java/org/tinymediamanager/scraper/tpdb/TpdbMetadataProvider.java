package org.tinymediamanager.scraper.tpdb;

import org.tinymediamanager.scraper.MediaProviderInfo;
import org.tinymediamanager.scraper.interfaces.IMediaProvider;

public class TpdbMetadataProvider implements IMediaProvider {
    private final MediaProviderInfo providerInfo;

    TpdbMetadataProvider() {
        providerInfo = createMediaProviderInfo();
    }

    public MediaProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public boolean isActive() {
        return isFeatureEnabled() && isApiKeyAvailable(getProviderInfo().getConfig().getValue("apiKey"));
    }

    public MediaProviderInfo createMediaProviderInfo() {
        MediaProviderInfo info = new MediaProviderInfo("tpdb", "movie",
                "theporndb.net", "<html><h3>ThePornDB (TPDB)</h3></html>",
                TpdbMetadataProvider.class.getResource("/org/tinymediamanager/scraper/tpdb/logo.png"));

        info.getConfig().addText("apiKey", "", true);
        info.getConfig().addSelect("type", new String[] {"Scene", "Movie", "JAV"}, "Scene");
        info.getConfig().load();

        return info;
    }
}
