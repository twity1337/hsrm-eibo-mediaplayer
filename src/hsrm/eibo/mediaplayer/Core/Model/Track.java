package hsrm.eibo.mediaplayer.Core.Model;

import hsrm.eibo.mediaplayer.Core.Exception.TrackUnsupportedFileTypeException;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.Util.MetadataParserTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Track implements MediaListElementInterface {

    private static final String METADATA_PARSER_THREAD_NAME = "Metadata Parser Thread";
    private Metadata metadata;
    private String trackPath;

    /**
     *
     * @param trackPath
     */
    public Track(String trackPath) throws IOException, TrackUnsupportedFileTypeException
    {
        this.metadataReady = new SimpleBooleanProperty(false);
        if(!ArrayUtils.contains(
                MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_AUDIO | MediaUtil.FILETYPE_GID_VIDEO),
                "*."+FilenameUtils.getExtension(trackPath))
        ){
            throw new TrackUnsupportedFileTypeException(FilenameUtils.getExtension(trackPath));
        }


        this.trackPath = trackPath;
        MetadataParserTask parserTask = new MetadataParserTask(this.trackPath);
        parserTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                metadata = (Metadata) event.getSource().getValue();
                setMetadataReady(true);
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

    public String getLocation() {
        return trackPath;
    }

    private String getUri()
    {
        return ("file:///" + trackPath.replace("\\", "/")).replace(" ", "%20");
    }

    private SimpleBooleanProperty metadataReady;

    public boolean isMetadataReady() {
        return metadataReady.get();
    }

    public SimpleBooleanProperty metadataReadyProperty() {
        return metadataReady;
    }

    public void setMetadataReady(boolean metadataReady) {
        this.metadataReady.set(metadataReady);
    }

    @Override
    public String toString() {
        if(isMetadataReady())
        {
            Metadata metadata = this.getMetadata();
            if(!metadata.getArtist().isEmpty() && !metadata.getTitle().isEmpty())
                return metadata.getArtist() + " - " + metadata.getTitle();

            if(!metadata.getTitle().isEmpty())
                return metadata.getTitle();
        }
        return this.getLocation();
    }
}
