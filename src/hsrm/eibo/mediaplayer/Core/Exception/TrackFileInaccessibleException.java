package hsrm.eibo.mediaplayer.Core.Exception;

public class TrackFileInaccessibleException extends  MediaPlayerException {
    public TrackFileInaccessibleException(String filePath) {
        super("Can't load file \"" + filePath + "\".");
    }
}
