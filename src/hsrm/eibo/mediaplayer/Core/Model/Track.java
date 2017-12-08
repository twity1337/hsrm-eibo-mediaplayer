package hsrm.eibo.mediaplayer.Core.Model;

import hsrm.eibo.mediaplayer.Core.Util.MetadataParserTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class Track {

    private static final String METADATA_PARSER_THREAD_NAME = "Metadata Parser Thread";
    private Metadata metadata;
    private String trackPath;

    /**
     *
     * @param trackPath
     */
    public Track(String trackPath) throws IOException
    {
        this.trackPath = trackPath;
        MetadataParserTask parserTask = new MetadataParserTask(this.trackPath);
        parserTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                metadata = (Metadata) event.getSource().getValue();
            }
        });
        Thread parserThread = new Thread(parserTask, METADATA_PARSER_THREAD_NAME);
        parserThread.setDaemon(true);
        parserThread.start();
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
