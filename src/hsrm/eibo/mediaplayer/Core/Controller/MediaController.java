package hsrm.eibo.mediaplayer.Core.Controller;
import hsrm.eibo.mediaplayer.Core.Exception.MediaPlayerException;
import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.beans.property.*;

import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Main business logic class
 * handles media player and provides methods for interaction with it.
 */
public class MediaController {

    /**
     * Integer array for shuffle functionality of player.
     * Playlist must retain given order to any time so this is used to determine order of replay
     */
    private int[] shuffleList;

    /**
     * JavaFx Mediaplayer thats selected for replay
     */
    private MediaPlayer currentMediaplayer;

    /**
     * the current Playlist
     */
    private Playlist playlist;

    /**
     * MediaController is a Singleton.
     */
    private static MediaController instance = new MediaController();

    /**
     * Since MediaController should only exists once its constructor is private (Singleton).
     * To get the instance of this object use this getter.
     * @return MediaController instance
     */
    public static MediaController getInstance() {
        return instance;
    }

    /**
     * Constructor initializes Properties.
     */
    private MediaController() {
        currentPlaybackIndex = new SimpleIntegerProperty(0);
        trackDuration = new SimpleDoubleProperty(0);
        inShuffleMode = new SimpleBooleanProperty(false);
        repeatMode = new SimpleObjectProperty<>(RepeatMode.NONE);
        playing = new SimpleBooleanProperty(false);
        stopped = new SimpleBooleanProperty(false);
        endOfMedia = new SimpleBooleanProperty(true);
        volume = new SimpleDoubleProperty(0.5);
        currentTime = new SimpleDoubleProperty(0);
        coverProperty = new SimpleObjectProperty<>(null);
        currentTrackMetadata = new SimpleObjectProperty<>();
        muted = new SimpleBooleanProperty(false);
    }

    /**
     * method to start javafx MediaPlayer.
     * If no mediaplayer is present or the mediaplayer object is faulty nothing happens.
     * if mediaplayer is not ready yet it starts as soon its status changes
     */
    public void play()
    {
        if (!this.mediaplayerSet()  || this.currentMediaplayer.getStatus() == MediaPlayer.Status.HALTED)
        {
            return;
        }
        if(this.currentMediaplayer.getStatus() == MediaPlayer.Status.UNKNOWN)
        {
            this.currentMediaplayer.statusProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != MediaPlayer.Status.READY)
                    return;
                instance.play();
            });

        }
        this.currentMediaplayer.play();

        this.setPlaying(true);
        this.setStopped(false);
    }

    /**
     * Method to pause playback.
     * The current time is preserved and the track can be resumed form there.
     */
    public void pause()
    {
        if (this.mediaplayerSet())
        {
            this.currentMediaplayer.pause();
            this.setPlaying(false);
            this.setStopped(true);
        }
    }

    /**
     * Method to skip to next track in playlist.
     * If shuffle mode: Next track is next in shufflelist.
     * If repeat is SINGLE: this method will not be called by continuous replay.
     * If at end of playlist and repeat is NONE: stop playback but selectfirst track in playlist.
     * If at end of playlist and repeat is ALL:  select first track in playlist.
     */
    public void skipToNext()
    {
        if (!mediaplayerSet())
            return;
        if (!isEndOfPlaylist())
        {
            this.setCurrentPlaybackIndex(this.currentPlaybackIndex.get()+1);
        }
        else if (this.getRepeatMode().equals(RepeatMode.ALL) && this.isEndOfPlaylist())
        {
            this.setCurrentPlaybackIndex(0);
        }
        this.setCurrentMediaplayer();
        // if continuous replay
        if (!this.isStopped())
            play();
    }

    /**
     * Method to skip back one track in playlist.
     * If currently at first track in playlist first track is selected again.
     */
    public void skipToPrevious()
    {
        if (!mediaplayerSet())
            return;
        if (this.getCurrentPlaybackIndex() >0)
            this.setCurrentPlaybackIndex(this.currentPlaybackIndex.get()-1);
        this.setCurrentMediaplayer();
        if (!this.isStopped())
            play();
    }

    /**
     * Method to select specific track by position in playlist.
     * @param playlistIndex position of track in playlist
     */
    public void skipToIndex(int playlistIndex)
    {
        this.setCurrentPlaybackIndex(playlistIndex);
        this.setCurrentMediaplayer();
    }

    /**
     * Method to set current time in currrent Mediaplayer
     * @param time in seconds to set mediaplayer.currentTime
     */
    public void seek(double time)
    {
        if (!this.mediaplayerSet())
            return;
        this.currentMediaplayer.seek(new Duration(time*1000));
    }

    /**
     * Method to set Playlist (List of track objects) for Controller
     * @param playlist to set controller to
     */
    public void setPlaylist(Playlist playlist)
    {
        this.playlist = playlist;
        //for shuffle mode create random list of integers for order reference
        if (isInShuffleMode())
            createShuffleList();
        this.setCurrentPlaybackIndex(0);
        this.setCurrentMediaplayer();
    }

    /**
     * Method to get current playlist of controller
     * @return playlist currently set in controller
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * method to set Mediaplayer in MediaController
     */
    private void setCurrentMediaplayer()
    {
        Track track = this.playlist.get(this.getCurrentPlaybackIndex());
        //stopping old mediaplayer and cleanup
        if(this.mediaplayerSet())
            this.currentMediaplayer.dispose();
        this.setPlaying(false);
        this.resetTrackDuration();
        // setting new mediaplayer and its eventhandler
        try {
            this.currentMediaplayer = track.getTrackMediaPlayer();
        } catch (MediaPlayerException e)
        {
            ErrorHandler err = ErrorHandler.getInstance();
            err.addError(e);
            err.notifyErrorObserver("Fehler beim Auswählen des Tracks");
        }
        bindListenersToCurrentMediaPlayer();
        System.out.println("Track: " + track.getLocation());
    }

    private boolean mediaplayerSet(){return this.currentMediaplayer != null;}

    private void rewind()
    {
        this.currentMediaplayer.seek(this.currentMediaplayer.getStartTime());
    }

    private void createShuffleList()
    {
        if (playlist == null)
            return;
        this.shuffleList = MediaUtil.generateShuffelList(this.playlist.size());
    }

    /**
     * method that handels automatic playback of playlist
     */
    private void playNext()
    {
        if (this.getRepeatMode().equals(RepeatMode.SINGLE))
            this.rewind();
        else
            this.skipToNext();
        this.play();
    }

    private boolean isEndOfPlaylist()
    {
        return (this.getCurrentPlaybackIndex() >= this.playlist.size()-1);
    }

    private void bindListenersToCurrentMediaPlayer() {
        this.currentMediaplayer.setOnPlaying(() -> instance.setPlaying(true));

        this.currentMediaplayer.setOnHalted(() -> {
            // TODO: job for exception handling
            System.err.println("Critical Error: player no longer useable");
            instance.currentMediaplayer.dispose();
            instance.currentMediaplayer=null;
        });

        this.currentMediaplayer.setOnPaused(() -> instance.setPlaying(false));

        this.currentMediaplayer.setOnStalled(() -> {
            System.out.println("Stream interrupted");
            instance.setPlaying(false);
        });

        this.currentMediaplayer.setOnStopped(() -> {
            instance.setPlaying(false);//stop resets playbacktime and mediaplayer wont respond to seek()
            instance.setStopped(true);
        });

        this.currentMediaplayer.setOnEndOfMedia(() -> {
            instance.setEndOfMedia();
            currentMediaplayer.stop();
            if (instance.isEndOfPlaylist() && instance.getRepeatMode() == RepeatMode.NONE){
                this.setCurrentPlaybackIndex(0);
                this.setStopped(true);
                this.setPlaying(false);
                this.setCurrentMediaplayer();
                return;
            }
            instance.playNext();
        });

        this.currentMediaplayer.setOnReady(() -> {
            instance.coverProperty.set((Image)currentMediaplayer.getMedia().getMetadata().get("image"));
            instance.trackDurationProperty().set(currentMediaplayer.getTotalDuration().toSeconds());
        });
        //bind volume property bidirectional
        this.currentMediaplayer.volumeProperty().bind(this.volumeProperty());
        // currentTime(double) listens to mediaplayer.currentTimeProperty(ReadOnlyObjectProperty<Duration>)
        this.currentMediaplayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> currentTimeProperty().set(newValue.toSeconds()));
        this.currentMediaplayer.muteProperty().bindBidirectional(this.muted);

        Track currentTrack = this.playlist.get(this.getCurrentPlaybackIndex());
        if (currentTrack.isMetadataReady())
            this.setCurrentTrackMetadata(currentTrack.getMetadata());
        else
            currentTrack.metadataReadyProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue)
                    setCurrentTrackMetadata(currentTrack.getMetadata());
            }));
    }
////////////////////////////////////////////////////////////////////////////////
//  Properties and classes                                                    //
////////////////////////////////////////////////////////////////////////////////

    private SimpleObjectProperty<Image> coverProperty;

    public SimpleObjectProperty<Image> coverProperty(){return coverProperty;}

    private BooleanProperty inShuffleMode;

    public boolean isInShuffleMode() {
        return inShuffleMode.get();
    }

    public BooleanProperty inShuffleModeProperty() {
        return inShuffleMode;
    }

    public void setInShuffleMode(boolean inShuffleMode) {
        this.inShuffleMode.set(inShuffleMode);
        //OFF->ON
        //if replay of tracks gets shuffled
        //playlist wont remember tracks already played
        //shuffled = !shuffled;
        if(this.isInShuffleMode())
        {
            createShuffleList();
            return;
        }
        //ON->OFF
        //if replay gets unshuffeld playlist continues to play
        // tracks in order(continuing track currently playing)
        if (playlist == null)
            return;
        this.setCurrentPlaybackIndex(this.shuffleList[this.getCurrentPlaybackIndex()]);
    }

    private BooleanProperty endOfMedia;

    public BooleanProperty endOfMediaProperty() {
        return endOfMedia;
    }

    private void setEndOfMedia() {
        this.endOfMedia.set(true);
    }

    private BooleanProperty playing;

    public boolean isPlaying() {
        return playing.get();
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    private void setPlaying(boolean playing) {
        this.playing.set(playing);
    }

    private BooleanProperty stopped;

    private boolean isStopped() {
        return stopped.get();
    }

    private void setStopped(boolean stopped) {
        this.stopped.set(stopped);
    }

    private DoubleProperty volume;

    public double getVolume() {
        return volume.get();
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    private DoubleProperty currentTime;

    public DoubleProperty currentTimeProperty() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime.set(currentTime);
    }

    public enum RepeatMode {NONE,SINGLE,ALL}

    private SimpleObjectProperty<RepeatMode> repeatMode;

    public RepeatMode getRepeatMode() {
        return repeatMode.get();
    }

    public SimpleObjectProperty<RepeatMode> repeatModeProperty() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode.set(repeatMode);
    }

    private DoubleProperty trackDuration;

    public double getTrackDuration() {
        return trackDuration.get();
    }

    public DoubleProperty trackDurationProperty() {
        return trackDuration;
    }

    private void resetTrackDuration() {
        this.trackDuration.set(0.1);
    }

    private SimpleObjectProperty<Metadata> currentTrackMetadata;

    public SimpleObjectProperty<Metadata> currentTrackMetadataProperty() {
        return currentTrackMetadata;
    }

    private void setCurrentTrackMetadata(Metadata currentTrackMetadata) {
        this.currentTrackMetadata.set(currentTrackMetadata);
    }

    private SimpleBooleanProperty muted;

    public boolean isMuted() {
        return muted.get();
    }

    public SimpleBooleanProperty mutedProperty() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted.set(muted);
    }

    private SimpleIntegerProperty currentPlaybackIndex;

    public SimpleIntegerProperty currentPlaybackIndexProperty() {
        return currentPlaybackIndex;
    }

    private int getCurrentPlaybackIndex() {
        if (isInShuffleMode())
            return shuffleList[this.currentPlaybackIndex.get()];
        return this.currentPlaybackIndex.get();
    }

    private void setCurrentPlaybackIndex(int currentPlaybackIndex) {
        this.currentPlaybackIndex.set(currentPlaybackIndex);
    }
}
