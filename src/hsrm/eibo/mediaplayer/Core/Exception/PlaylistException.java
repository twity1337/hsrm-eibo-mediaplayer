package hsrm.eibo.mediaplayer.Core.Exception;

import java.util.ArrayList;

public class PlaylistException extends MediaPlayerException implements HasAdditionalInformation {
    private ArrayList<String> failedFilepaths = new ArrayList<>();

    public void addFailedFilePath(String path){failedFilepaths.add(path);}

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
