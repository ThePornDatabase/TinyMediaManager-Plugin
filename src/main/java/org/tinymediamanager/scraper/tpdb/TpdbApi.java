package org.tinymediamanager.scraper.tpdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.entities.Person;
import org.tinymediamanager.scraper.MediaMetadata;
import org.tinymediamanager.scraper.entities.MediaCertification;
import org.tinymediamanager.scraper.exceptions.NothingFoundException;
import org.tinymediamanager.scraper.exceptions.ScrapeException;
import org.tinymediamanager.scraper.tpdb.entities.*;
import org.tinymediamanager.scraper.tpdb.service.Controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TpdbApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TpdbApi.class);
    private final Controller controller;

    public enum SceneType {
        SCENE,
        MOVIE,
        JAV,
    }

    public TpdbApi(String apiKey) {
        this.controller = new Controller(false);
        controller.setApiKey(apiKey);
    }

    private String getId() {
        return "tpdb";
    }

    public MediaMetadata setMediaMetadata(SceneEntity scene) {
        MediaMetadata result = new MediaMetadata(getId());

        if (scene == null) {
            return result;
        }

        result.setId(getId(), scene.id);
        result.setCertifications(getCertifications());
        result.setTitle(scene.title);
        result.setPlot(scene.description);

        try {
            result.setProductionCompanies(getProductionCompanies(scene.site));
        } catch (Exception e) {
            LOGGER.error("error site: {}", e.getMessage());
        }

        result.setReleaseDate(getDate(scene.date));
        result.setYear(getYear(result.getReleaseDate()));

        result.setCastMembers(getCastMembers(scene.performers, scene.directors));
        result.setTags(scene.tags.stream().map(o -> o.name).sorted().collect(Collectors.toList()));

        return result;
    }

    public SceneType getType(String type) {
        switch (type) {
            case "Scene":
                return SceneType.SCENE;
            case "Movie":
                return SceneType.MOVIE;
            case "JAV":
                return SceneType.JAV;
        }

        return null;
    }

    public List<SceneEntity> searchScenes(String q, SceneType type) throws ScrapeException {
        SceneSearch search = null;
        try {
            switch (type) {
                case SCENE:
                    search = controller.getScenesFromQuery(q);
                    break;
                case MOVIE:
                    search = controller.getMoviesFromQuery(q);
                    break;
                case JAV:
                    search = controller.getJAVFromQuery(q);
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("error search: {}", e.getMessage());
            throw new ScrapeException(e);
        }

        if (search == null || search.data == null) {
            LOGGER.warn("no result found");
            throw new NothingFoundException();
        }

        return search.data;
    }

    public SceneEntity getScene(String id, SceneType type) throws ScrapeException {
        SceneGet search = null;
        try {
            switch (type) {
                case SCENE:
                    search = controller.getSceneFromId(id);
                    break;
                case MOVIE:
                    search = controller.getMovieFromId(id);
                    break;
                case JAV:
                    search = controller.getJAVFromId(id);
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("error scene: {}", e.getMessage());
            throw new ScrapeException(e);
        }

        if (search == null || search.data == null) {
            LOGGER.warn("no result found");
            throw new NothingFoundException();
        }

        return search.data;
    }

    public Date getDate(String date) {
        Date date_obj = null;

        if (date != null) {
            try {
                date_obj = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (Exception e) {
                LOGGER.error("error date: {}", e.getMessage());
            }
        }

        return date_obj;
    }

    public Integer getYear(Date date) {
        Integer year = null;

        if (date != null) {
            try {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                year = calendar.get(Calendar.YEAR);
            } catch (Exception e) {
                LOGGER.error("error year: {}", e.getMessage());
            }
        }

        return year;
    }

    public List<MediaCertification> getCertifications() {
        List<MediaCertification> certifications = new ArrayList<>();

        certifications.add(MediaCertification.UNKNOWN);

        return certifications;
    }

    public List<Person> getCastMembers(List<PerformerEntity> performers, List<DirectorEntity> directors) {
        List<Person> castMembers = new ArrayList<>();

        for (PerformerEntity performer : performers) {
            Person person = new Person();
            person.setType(Person.Type.ACTOR);
            person.setThumbUrl(performer.image);

            if (performer.parent != null) {
                person.setId(getId(), performer.parent.id);
                person.setName(performer.parent.name);
                person.setRole(performer.parent.extras.gender);
                person.setProfileUrl(Const.PerformerURL + performer.parent.slug);
            } else {
                person.setName(performer.name);
                person.setProfileUrl(Const.PerformerSiteURL + performer.id);
            }

            castMembers.add(person);
        }

        for (DirectorEntity director : directors) {
            Person person = new Person();
            person.setType(Person.Type.DIRECTOR);
            person.setName(director.name);

            castMembers.add(person);
        }

        return castMembers;
    }

    public List<String> getProductionCompanies(SiteEntity site) throws ScrapeException {
        List<String> productionCompanies = new ArrayList<>();

        productionCompanies.add(site.name);

        if (site.parent_id != null && !site.id.equals(site.parent_id)) {
            SiteGet parent_site;
            try {
                parent_site = controller.getSiteFromId(Integer.toString(site.parent_id));
            } catch (Exception e) {
                LOGGER.error("error scene: {}", e.getMessage());
                throw new ScrapeException(e);
            }

            productionCompanies.add(parent_site.data.name);
        }

        if (site.network_id != null && !site.id.equals(site.network_id)) {
            SiteGet network_site;
            try {
                network_site = controller.getSiteFromId(Integer.toString(site.network_id));
            } catch (Exception e) {
                LOGGER.error("error scene: {}", e.getMessage());
                throw new ScrapeException(e);
            }

            productionCompanies.add(network_site.data.name);
        }

        return productionCompanies;
    }
}
