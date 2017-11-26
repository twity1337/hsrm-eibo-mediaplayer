package hsrm.eibo.mediaplayer;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private static PlaylistManager instance = new PlaylistManager();
    private List<Playlist> playlistList;

    public static PlaylistManager getInstance() {
        return instance;
    }


    private PlaylistManager() {
        playlistList = new ArrayList<Playlist>();
    }

}
