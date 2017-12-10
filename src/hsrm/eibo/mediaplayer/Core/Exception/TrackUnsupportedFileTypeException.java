package hsrm.eibo.mediaplayer.Core.Exception;

public class TrackUnsupportedFileTypeException extends Exception {
    public TrackUnsupportedFileTypeException(String extension) {
        super("Filetype '" + extension + "' is not supported.");
    }
}
