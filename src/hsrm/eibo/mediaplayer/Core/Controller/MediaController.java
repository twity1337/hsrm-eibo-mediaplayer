package hsrm.eibo.mediaplayer.Core.Controller;
import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;

import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


//TODO: repeat mode (not just whole list but single track), volume state handling, progression handling (notify when track ended)
public class MediaController {
    //private ArrayList<MediaPlayer> mediaplayerList;//TODO: if loading on demand isnt fast enough, reconsider
    private int[] shuffleList;
    private int currentTrackInPlaybackIndex;
    private MediaPlayer currentMediaplayer;
    private Playlist playlist;
    private static MediaController instance = new MediaController();

    public static MediaController getInstance() {
        return instance;
    }

    private MediaController() {
        //TODO: load config on startup(maybe)
        this.currentTrackInPlaybackIndex = 0;
        inShuffleMode = new SimpleBooleanProperty(false);
        inRepeatingMode = new SimpleObjectProperty<RepeatMode>(RepeatMode.NONE);
        playing = new SimpleBooleanProperty(false);
        stopped = new SimpleBooleanProperty(false);
        endOfMedia = new SimpleBooleanProperty(true);
        volume = new SimpleDoubleProperty(0.5);
        currentTime = new SimpleDoubleProperty(0);
    }


    public void play()
    {
        if (this.currentMediaplayer == null
            || this.currentMediaplayer.getStatus() == MediaPlayer.Status.UNKNOWN
            || this.currentMediaplayer.getStatus() == MediaPlayer.Status.HALTED)
        {
            return;
        }
        this.currentMediaplayer.play();
    }

    public void pause()
    {

    }

    public void skipToNext()
    {
        this.currentMediaplayer.dispose();
        if (!endOfPlaylist())
        {
            currentTrackInPlaybackIndex++;
        }
        else if (this.getInRepeatingMode().equals(RepeatMode.ALL) && this.endOfPlaylist())
        {
            this.currentTrackInPlaybackIndex = 0;
        }
        this.setCurrentMediaplayer();
            return;
    }

    public void skipToPrevious()
    {
        this.currentMediaplayer.dispose();
        if (this.currentTrackInPlaybackIndex>0)
            currentTrackInPlaybackIndex--;
        this.setCurrentMediaplayer();
    }

    //user wont see order by which tracks will be played in shuffel mode;
    //playlist should retain order at all times(especially in view)
    public void toggleShuffle()
    {
        //ON->OFF
        //if replay gets unshuffeld playlist continues to play
        // tracks in order(continuing track currently playing)
        if(this.isInShuffleMode())
        {
            this.setInShuffleMode(false);
            this.currentTrackInPlaybackIndex = this.shuffleList[this.currentTrackInPlaybackIndex];
            return;
        }
        //OFF->ON
        //if replay of tracks gets shuffled
        //playlist wont remember tracks already played
        //shuffled = !shuffled;
        createShuffleList();
    }

    /**
     *
     * @param time in seconds mediaplayer.currentTime should be set to
     */
    public void seek(double time)
    {
        this.currentMediaplayer.seek(new Duration(time*1000));
    }

    public void setPlaylist(Playlist playlist)
    {
        this.playlist=playlist;
        this.currentTrackInPlaybackIndex=0;
        this.setCurrentMediaplayer();
    }

    public void appendPlaylist(Playlist apendix)
    {
        this.playlist.addAll(apendix);
        if (isInShuffleMode())
            createShuffleList();
    }

    /**
     * method to add one or more Track objects to current playlist
     * TODO: this should be part of playlistmanager
     * @param tracksToAdd Track[]
     */
    public void addTrackToPlaylist(Track...tracksToAdd)
            throws PlaylistIOException
    {
        Playlist newTracks = new Playlist();
        for (Track trackToAdd : tracksToAdd)
            newTracks.add(trackToAdd);
        this.playlist.addAll(newTracks);
        if (isInShuffleMode())
            shuffleList = MediaUtil.generateShuffelList(playlist.size());
    }

    /**
     * method to set Mediaplayer in MediaController
     * @param track from where MediaPlayer objekt is extracted
     */
    public void setCurrentMediaplayer(Track track)
    {
        //stopping old mediaplayer and cleanup
        if (this.isPlaying())
            currentMediaplayer.stop();
        //TODO: test if dispose does stop
        this.currentMediaplayer.dispose();
        // setting new mediaplayer and its eventhandler
        this.currentMediaplayer = track.getTrackMediaPlayer();

        this.currentMediaplayer.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                instance.setPlaying(true);
            }
        });

        this.currentMediaplayer.setOnHalted(new Runnable() {
            @Override
            public void run() {
                System.out.println("Critical Error: player no longer useable");
                instance.currentMediaplayer.dispose();
                instance.currentMediaplayer=null;
            }
        });

        this.currentMediaplayer.setOnPaused(new Runnable() {
            @Override
            public void run() {
                instance.setPlaying(false);
            }
        });

        this.currentMediaplayer.setOnStalled(new Runnable() {
            @Override
            public void run() {
                System.out.println("Stream interrupted");
                instance.setPlaying(false);
            }
        });

        this.currentMediaplayer.setOnStopped(new Runnable() {
            @Override
            public void run() {
                instance.setPlaying(false);//stop resets playbacktime and mediaplayer wont respond to seek()
                instance.setStopped(true);
            }
        });

        this.currentMediaplayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                instance.setEndOfMedia(true);
                instance.playNext();
            }
        });
        //bind volume property bidirectional
        this.currentMediaplayer.volumeProperty().
                bindBidirectional(this.volumeProperty());
        //bind currentTime(double) to mediaplayer.currentTimeProperty(ReadOnlyObjectProperty<Duration>)
        this.currentTimeProperty().bind(new DurationToDoubleBinding(
                this.currentMediaplayer.currentTimeProperty()));
        //TODO: talk about duration slider handling
    }

    public void setCurrentMediaplayer()
    {this.setCurrentMediaplayer(
            this.playlist.get(currentTrackInPlaybackIndex));}

    public MediaPlayer getCurrentMediaplayer()
    {
        return currentMediaplayer;
    }

    public void rewind()
    {
        this.currentMediaplayer.seek(this.currentMediaplayer.getStartTime());
    }

    private void createShuffleList()
    {//TODO: maybe put this in event Playlist.changed or something
        this.shuffleList = MediaUtil.generateShuffelList(this.playlist.size());
    }

    /**
     * method that handels automatic playback of playlist
     */
    private void playNext()
    {
        if (this.getInRepeatingMode().equals(RepeatMode.SINGLE))
        {
            this.rewind();
        } else {
            this.skipToNext();
        }
        this.play();
    }

    private boolean endOfPlaylist()
    {
        return (this.currentTrackInPlaybackIndex >= this.playlist.size()-1);
    }
////////////////////////////////////////////////////////////////////////////////
//  Properties and classes                                                    //
////////////////////////////////////////////////////////////////////////////////

    /**
     * returns double (in seconds)
     */
    private class DurationToDoubleBinding extends ObjectBinding<Double>{
        ReadOnlyObjectProperty<Duration> d;
        public DurationToDoubleBinding(ReadOnlyObjectProperty<Duration> durationObject)
        {
            super.bind(durationObject);
            d = durationObject;
        }
        @Override
        protected Double computeValue() {
            return d.getValue().toSeconds();
        }
    }

    private BooleanProperty inShuffleMode;

    public boolean isInShuffleMode() {
        return inShuffleMode.get();
    }

    public BooleanProperty inShuffleModeProperty() {
        return inShuffleMode;
    }

    public void setInShuffleMode(boolean inShuffleMode) {
        this.inShuffleMode.set(inShuffleMode);
    }

    private BooleanProperty endOfMedia;

    public boolean isEndOfMedia() {
        return endOfMedia.get();
    }

    public BooleanProperty endOfMediaProperty() {
        return endOfMedia;
    }

    public void setEndOfMedia(boolean endOfMedia) {
        this.endOfMedia.set(endOfMedia);
    }

    private BooleanProperty playing;

    public boolean isPlaying() {
        return playing.get();
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing.set(playing);
    }

    private BooleanProperty stopped;

    public boolean isStopped() {
        return stopped.get();
    }

    public BooleanProperty stoppedProperty() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
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

    public double getCurrentTime() {
        return currentTime.get();
    }

    public DoubleProperty currentTimeProperty() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime.set(currentTime);
    }

    enum RepeatMode {NONE,SINGLE,ALL}

    private SimpleObjectProperty<RepeatMode> inRepeatingMode;

    public RepeatMode getInRepeatingMode() {
        return inRepeatingMode.get();
    }

    public SimpleObjectProperty<RepeatMode> inRepeatingModeProperty() {
        return inRepeatingMode;
    }

    public void setInRepeatingMode(RepeatMode inRepeatingMode) {
        this.inRepeatingMode.set(inRepeatingMode);
    }
}
