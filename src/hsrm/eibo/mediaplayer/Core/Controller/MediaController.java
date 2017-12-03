package hsrm.eibo.mediaplayer.Core.Controller;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.scene.media.MediaPlayer.Status;

import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
//TODO: repeat mode (not just whole list but single track), volume state handling, progression handling (notify when track ended)
public class MediaController {
    private ArrayList<MediaPlayer> mediaplayerList;
    private int[] shuffleList;
    private int currentTrackIndex;
    private MediaPlayer currentMediaplayer;
    private Playlist playlist;
    private boolean repeating;
    private boolean shuffled;
    private boolean endOfMedia;
    private static MediaController ourInstance = new MediaController();

    public static MediaController getInstance() {
        return ourInstance;
    }

    private MediaController() {
        //TODO: load config on startup(maybe)
        this.repeating =false;
        this.shuffled =false;
        this.currentTrackIndex = 0;
    }

    public void togglePlayPause()
    {
        if (currentTrackIndex < 0)
            currentTrackIndex=0;
        //TODO: add user promt to select playlist if no selected so far
        if (playlist == null)
            return;

        if(this.currentMediaplayer == null)
            this.currentMediaplayer = this.playlist.get(currentTrackIndex).getTrackMediaPlayer();

        Status status = this.currentMediaplayer.getStatus();

        if(status == Status.HALTED || status == Status.UNKNOWN)
            return;

        if(status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)
        {
            if(this.endOfMedia)
            {
                this.currentMediaplayer.seek(this.currentMediaplayer.getStartTime());
                this.endOfMedia = false;
            }
            this.currentMediaplayer.play();
        } else {
            this.currentMediaplayer.pause();
        }

    }

    public void skipToNext(){
        this.currentMediaplayer.stop();
        this.currentMediaplayer = this.mediaplayerList.get(nextPlayerIndex());
        this.currentMediaplayer.play();
    }

    public void skipToPrevious()
    {
        this.currentMediaplayer.stop();
        this.currentMediaplayer = this.mediaplayerList.get(previousPlayerIndex());
        this.currentMediaplayer.play();
    }

    public void setPlaylist(Playlist playlist)
    {
        this.currentMediaplayer.stop();
        this.currentMediaplayer.dispose();
        this.playlist = playlist;
        this.currentTrackIndex=0;
        if (shuffled)
            this.shuffleList = MediaUtil.generateShuffelList(this.playlist.size());
        this.mediaplayerList = MediaUtil.generateMediaplayerList(this.playlist);
        this.currentMediaplayer = this.mediaplayerList.get(nextPlayerIndex());
    }

    private int nextPlayerIndex()
    {
        //end of playlist = -1
        currentTrackIndex = ++currentTrackIndex >= playlist.size() ?
                (repeating ? currentTrackIndex: -1) : currentTrackIndex;
        if (currentTrackIndex<0 || !shuffled)
            return currentTrackIndex;
        return shuffleList[currentTrackIndex];
    }

    private int previousPlayerIndex()
    {
        if (--currentTrackIndex < 0 || !shuffled)
            return currentTrackIndex;
        return shuffleList[currentTrackIndex];
    }

    //user wont see order by which tracks will be played in shuffel mode;
    //playlist should retain order at all times(especially in view)
    public void toggleShuffle()
    {
        //ON->OFF
        //if replay gets unshuffeld playlist continues to play
        // tracks in order(continuing track currently playing)
        if(shuffled)
        {
            shuffled = !shuffled;
            currentTrackIndex = shuffleList[currentTrackIndex];
            return;
        }
        //OFF->ON
        //if replay of tracks gets shuffled
        //playlist wont remember tracks already played
        shuffled = !shuffled;
        shuffleList = MediaUtil.generateShuffelList(this.playlist.size());
    }

    /**
     * method to add one or more Track objects to current playlist
     * TODO: currently only adding at end of list
     * @param tracksToAdd Track[]
     */
    public void addTrackToPlaylist(Track...tracksToAdd)
    {
        Playlist newTracks = new Playlist();
        for (Track trackToAdd : tracksToAdd)
            newTracks.add(trackToAdd);
        this.mediaplayerList.addAll(MediaUtil.generateMediaplayerList(newTracks));
        this.playlist.addAll(newTracks);
        if (shuffled)
            shuffleList = MediaUtil.generateShuffelList(playlist.size());
    }
}
