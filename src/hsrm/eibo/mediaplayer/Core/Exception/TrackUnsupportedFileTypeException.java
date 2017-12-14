package hsrm.eibo.mediaplayer.Core.Exception;

import org.apache.commons.io.FilenameUtils;

public class TrackUnsupportedFileTypeException extends Exception {
    public TrackUnsupportedFileTypeException(String filePath) {
        super("Can't load file \"" + filePath + "\". Filetype '" + FilenameUtils.getExtension(filePath) + "' is not supported.");
    }
}
