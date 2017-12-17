package hsrm.eibo.mediaplayer.Core.Exception;

import java.util.ArrayList;

/**
 * Exception thrown on error loading media files from file paths to playlist.
 * Collects multiple file paths of one parsing thread and throws them at once.
 */
public class PlaylistException extends MediaPlayerException implements HasAdditionalInformation {
    private ArrayList<String> failedFilepaths = new ArrayList<>();

    /**
     * Method to add String file path of file unable to load to plpaylist
     * @param path to file not loaded
     */
    public void addFailedFilePath(String path){failedFilepaths.add(path);}

    /**
     * method to extract file paths not loaded from exception.
     * @return
     */
    @Override
    public String getAddionalInformationMessage()
    {
        StringBuilder b = new StringBuilder();
        for(String c : failedFilepaths.toArray(new String[]{}))
        {
            b.append(c).append("\n");
        }
        return b.toString();
    }
}
