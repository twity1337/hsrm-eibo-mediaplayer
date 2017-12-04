package hsrm.eibo.mediaplayer.Core.Exception;

import java.util.ArrayList;

public class PlaylistIOException extends PlaylistException{
    private ArrayList<String> failedFilepaths = new ArrayList<>();

    public void addFailedFilePath(String path){failedFilepaths.add(path);}

}
