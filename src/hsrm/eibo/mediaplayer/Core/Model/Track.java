package hsrm.eibo.mediaplayer.Core.Model;

import hsrm.eibo.mediaplayer.Core.Exception.TrackFileInaccessibleException;
import hsrm.eibo.mediaplayer.Core.Exception.TrackUnsupportedFileTypeException;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.Util.MetadataParserTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;

/**
 * Track objects contain the Metadata of a media file and the system path to the media file.
 * On creation metadata will be read in a different thread and published on completing the thread.
 * MediaListElementInterface provides ClassCast safety.
 */
public class Track implements MediaListElementInterface {

    /**
     * name of the metadata parsing thread
     */
    private static final String METADATA_PARSER_THREAD_NAME = "Metadata Parser Thread";
    /**
     * model.Metadata instance with metadata of this track
     */
    private Metadata metadata;
    /**
     * system path to media file
     */
    private String trackPath;

    /**
     * Constructor for Track object expects system path to media file
     * @param trackPath system path to media file
     * @throws TrackUnsupportedFileTypeException on encountering unsupported file extension
     */
    public Track(String trackPath) throws IOException, TrackUnsupportedFileTypeException
    {
        this.metadataReady = new SimpleBooleanProperty(false);
        if(!ArrayUtils.contains(
                MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_AUDIO | MediaUtil.FILETYPE_GID_VIDEO),
                "*."+FilenameUtils.getExtension(trackPath))
        ){
            throw new TrackUnsupportedFileTypeException(trackPath);
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

    /**
     * Method to return Metadata of track
     * @return Model.Metadata object containing metadata of track
     */
    public Metadata getMetadata()
    {
        return this.metadata;
    }

    /**
     * Method to create javafx.Mediaplayer
     * @return MedaiPlayer containing medai file of track
     * @throws TrackFileInaccessibleException on file IO operation error containing system path + name of file
     * @throws TrackUnsupportedFileTypeException on opening unknown/unsuppoerted file type
     */
    public MediaPlayer getTrackMediaPlayer() throws TrackFileInaccessibleException, TrackUnsupportedFileTypeException
    {
        Media media;
        try {
            media = new Media(this.getUri());

        }catch (MediaException e)
        {
            if (e.getType() == MediaException.Type.MEDIA_INACCESSIBLE || e.getType() == MediaException.Type.MEDIA_UNAVAILABLE)
                throw new TrackFileInaccessibleException(this.getLocation());
            else if(e.getType() == MediaException.Type.MEDIA_UNSUPPORTED)
                throw new TrackUnsupportedFileTypeException(this.getLocation());
            else
                throw e;
        }

        return new MediaPlayer(media);
    }

    /**
     * Method to return system path of media file (of track).
     * @return String containing system path of media file
     */
    public String getLocation() {
        return trackPath;
    }

    /**
     * Method to convert system path to uri
     * @return system path as uri of media file
     */
    private String getUri()
    {
        return ("file:///" + trackPath.replace("\\", "/")).replace(" ", "%20");
    }

    /**
     * Property indicating if metadata are loaded
     */
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

    /**
     * Method to get metadata information (artist and title) as String
     * @return String containing information of metadata object as String, if empty return file name
     */
    @Override
    public String toString() {
        if(isMetadataReady())
        {
            Metadata metadata = this.getMetadata();
            if(metadata.getArtist() != null && metadata.getTitle() != null)
                return metadata.getArtist() + " - " + metadata.getTitle();

            if(metadata.getTitle() != null)
                return metadata.getTitle();
        }
        return FilenameUtils.getName(this.getLocation());
    }
}
