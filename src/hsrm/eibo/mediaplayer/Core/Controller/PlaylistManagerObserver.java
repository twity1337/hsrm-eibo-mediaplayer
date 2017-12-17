package hsrm.eibo.mediaplayer.Core.Controller;

/**
 * A custom observer interface for Playlist manager
 */
@FunctionalInterface
public interface PlaylistManagerObserver{

    void update(PlaylistManager o);
}
