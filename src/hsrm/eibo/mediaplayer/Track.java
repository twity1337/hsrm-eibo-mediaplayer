package hsrm.eibo.mediaplayer;


import javafx.scene.media.Media;

public class Track {
    private String mediaLocation, title, album, artist;
    Media media;

    public Track(String mediaLocation) {
        this.mediaLocation = mediaLocation;
        media = new Media(mediaLocation);
        this.title = (String)media.getMetadata().get("title");
        this.title = (String)media.getMetadata().get("album");
        this.title = (String)media.getMetadata().get("artist");
    }
}
