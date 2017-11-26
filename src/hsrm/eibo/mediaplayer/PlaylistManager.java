package hsrm.eibo.mediaplayer;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private static PlaylistManager ourInstance = new PlaylistManager();
    List<Playlist> playlistList;

    public static PlaylistManager getInstance() {
        return ourInstance;
    }


    private PlaylistManager() {
        playlistList = new ArrayList<Playlist>();
    }

}
