package hsrm.eibo.mediaplayer.Core.Exception;

public class FileInaccessibleException extends  MediaPlayerException {
    public FileInaccessibleException(String filePath) {
        super("Can't load file \"" + filePath + "\".");
    }
}
