package hsrm.eibo.mediaplayer;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer.Status;

import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.ArrayList;

public class MediaController extends Pane{
    private MediaView mediaView;
    private ArrayList<MediaPlayer> mediaPlayerList;
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

    public void play()
    {
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

    //method for testing to directly inject media
    public void setTrack(MediaPlayer mediaPlayer) {
        this.currentPlayer=mediaPlayer;
    }
}
