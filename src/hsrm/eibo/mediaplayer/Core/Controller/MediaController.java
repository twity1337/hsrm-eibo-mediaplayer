package hsrm.eibo.mediaplayer.Core.Controller;
import javafx.scene.media.MediaPlayer.Status;

import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MediaController {
    private MediaView mediaView;
    //private ArrayList<MediaPlayer> mediaPlayerList;
    //private ArrayList<>
    private MediaPlayer currentPlayer;
    private Playlist playlist;
    private boolean repeat;
    private boolean endOfMedia;
    private static MediaController ourInstance = new MediaController();

    public static MediaController getInstance() {
        return ourInstance;
    }

    private MediaController() {

    }

    public void togglePlayPause()
    {   //TODO: test if mediaplayer set and throw excption

        if(currentPlayer == null && playlist != null)
            this.currentPlayer = playlist.get(0).getTrackMediaPlayer();

        Status status = currentPlayer.getStatus();

        if(status == Status.HALTED || status == Status.UNKNOWN)
            return;

        if(status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)
        {
            if(endOfMedia)
            {
                currentPlayer.seek(currentPlayer.getStartTime());
                endOfMedia = false;
            }
            currentPlayer.play();
        } else {
            currentPlayer.pause();
        }

    }

    //TODO: stop/aktuellen player stoppen /neuen einfügen skip next prev
    public void skipToNext(){}

    public void setPlaylist(Playlist playlist)
    {
        this.playlist = playlist;
    }
}
