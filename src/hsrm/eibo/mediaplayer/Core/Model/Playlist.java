package hsrm.eibo.mediaplayer.Core.Model;

import javafx.scene.media.Media;

import java.util.List;

public class Playlist {
    private List<Media> medias;
    private String name;
    private String locationUri;

    public Playlist(List<Media> medias, String name, String locationUri) {
        this.medias = medias;
        this.name = name;
        this.locationUri = locationUri;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationUri() {
        return locationUri;
    }
}
