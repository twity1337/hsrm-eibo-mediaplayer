package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager extends ArrayList<Playlist>{
    private static PlaylistManager instance = new PlaylistManager();


    public static PlaylistManager getInstance() {
        return instance;
    }


    private PlaylistManager(){}

}
