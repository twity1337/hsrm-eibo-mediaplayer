package hsrm.eibo.mediaplayer;

import javafx.scene.media.Media;
import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import javafx.scene.media.MediaPlayer;

import java.net.URI;

public class Track {

    private Metadata metadata;

    //path String for Media object
    private String url;
    //path String for tika metadata
    private String path;

    /**
     * TODO: decide witch format will be given to as path parameter
     * @param path
     */
    public Track(String path)
    {
        this.url = path;
        try {this.path = (new URI(path)).getPath();}catch (Exception e){}
        if(path != null)
            this.metadata = MetadataFactory.createMetadata(this.path);
    }

    public Metadata getMetadata()
    {
        return this.metadata;
    }

    public MediaPlayer getTrackMediaPlayer()
    {
        return new MediaPlayer(new Media(url));
    }
}
