package hsrm.eibo.mediaplayer.Core.Model;

import hsrm.eibo.mediaplayer.Core.Util.MetadataService;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class Track {

    private Metadata metadata;
    private String trackPath;

    /**
     *
     * @param trackPath
     */
    public Track(String trackPath) throws IOException
    {
        this.trackPath = trackPath;
        MetadataService service = new MetadataService();
        service.setPath(trackPath);
        service.setOnSucceeded(event ->
                metadata = (Metadata)event.getSource().getValue());
    }

    public Metadata getMetadata()
    {
        return this.metadata;
    }

    public MediaPlayer getTrackMediaPlayer()
    {
        return new MediaPlayer(new Media(this.getUri()));
    }

    public String getTrackPath() {
        return trackPath;
    }

    private String getUri()
    {
        return ("file:///" + trackPath.replace("\\", "/")).replace(" ", "%20");
    }
}
