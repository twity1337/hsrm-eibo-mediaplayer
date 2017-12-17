package hsrm.eibo.mediaplayer.Core.Exception;

/**
 * Exception thrown when media file of track cannot be accessed.
 */
public class TrackFileInaccessibleException extends  MediaPlayerException {
    public TrackFileInaccessibleException(String filePath) {
        super("Can't load file \"" + filePath + "\".");
    }
}
