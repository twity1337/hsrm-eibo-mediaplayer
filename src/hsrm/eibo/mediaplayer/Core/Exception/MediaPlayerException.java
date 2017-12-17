package hsrm.eibo.mediaplayer.Core.Exception;

public class MediaPlayerException extends Exception {
    public MediaPlayerException() {
    }

    public MediaPlayerException(String message) {
        super(message);
    }

    public MediaPlayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MediaPlayerException(Throwable cause) {
        super(cause);
    }

    public MediaPlayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
