package hsrm.eibo.mediaplayer.Core.Exception;

import org.apache.commons.io.FilenameUtils;

/**
 * Exception thrown on error loading media file due to unsupported file type
 */
public class TrackUnsupportedFileTypeException extends MediaPlayerException {
    public TrackUnsupportedFileTypeException(String filePath) {
        super("Can't load file \"" + filePath + "\". Filetype '" + FilenameUtils.getExtension(filePath) + "' is not supported.");
    }
}
