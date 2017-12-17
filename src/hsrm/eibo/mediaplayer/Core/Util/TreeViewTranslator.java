package hsrm.eibo.mediaplayer.Core.Util;

import hsrm.eibo.mediaplayer.Core.Controller.PlaylistManager;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;

/**
 * A helper class for translating absolute treeview indices to relative playlist indices.
 */
public class TreeViewTranslator {

    private PlaylistManager playlistManager = PlaylistManager.getInstance();
    private int selectedIndex = 0;
    private int passedPlaylistIndices = 0;


    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * Maps a given index to a playlist in TreeView.
     * @return
     */
    public Playlist lookupPlaylistForTreeIndex() {
        Playlist[] playlists = playlistManager.toArray(new Playlist[]{});
        int i = 0;
        for(Playlist pl : playlists)
        {
            i += pl.size() + 1;
            if(i >= selectedIndex)
                return pl;
            passedPlaylistIndices = i;
        }
        return null;
    }

    /**
     * Translates the given index from Playlist-TreeView to a relative index in playlist.
     * @return the relative index in current playlist.
     */
    public int getRelativePlaylistIndex()
    {
        return selectedIndex - passedPlaylistIndices - 1;
    }
}
