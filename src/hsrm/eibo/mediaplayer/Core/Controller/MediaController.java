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
    private static MediaController ourInstance = new MediaController();

    public static MediaController getInstance() {
        return ourInstance;
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

    }

    public void skipToPrevious()
    {

    }

    private int previousPlayerIndex()
    {
        if (--currentTrackInPlaybackIndex < 0 || !this.isInShuffleMode())
            return currentTrackInPlaybackIndex;
        return shuffleList[currentTrackInPlaybackIndex];
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
            currentTrackInPlaybackIndex = shuffleList[currentTrackInPlaybackIndex];
            return;
        }
        //OFF->ON
        //if replay of tracks gets shuffled
        //playlist wont remember tracks already played
        //shuffled = !shuffled;
        shuffleList = MediaUtil.generateShuffelList(this.playlist.size());
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
        this.mediaplayerList.addAll(
                MediaUtil.generateMediaplayerList(newTracks));
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
        // setting new mediaplayer and its eventhandler
        this.currentMediaplayer = track.getTrackMediaPlayer();

        this.currentMediaplayer.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                ourInstance.setPlaying(true);
            }
        });

        this.currentMediaplayer.setOnHalted(new Runnable() {
            @Override
            public void run() {
                System.out.println("Critical Error: player no longer useable");
                ourInstance.currentMediaplayer.dispose();
                ourInstance.currentMediaplayer=null;
            }
        });

        this.currentMediaplayer.setOnPaused(new Runnable() {
            @Override
            public void run() {
                ourInstance.setPlaying(false);
            }
        });

        this.currentMediaplayer.setOnStalled(new Runnable() {
            @Override
            public void run() {
                System.out.println("Stream interrupted");
                ourInstance.setPlaying(false);
            }
        });

        this.currentMediaplayer.setOnStopped(new Runnable() {
            @Override
            public void run() {
                ourInstance.setPlaying(false);//stop resets playbacktime and mediaplayer wont respond to seek()
                ourInstance.setStopped(true);
            }
        });

        this.currentMediaplayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                ourInstance.setEndOfMedia(true);
                ourInstance.commence();
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

    private void seekBeginning()
    {
        this.currentMediaplayer.seek(this.currentMediaplayer.getStartTime());
    }

    private void commence()
    {
        if (this.getInRepeatingMode().equals(RepeatMode.SINGLE))
        {
            this.seekBeginning();
            this.play();
            return;
        }

        if (this.currentTrackInPlaybackIndex >= this.playlist.size())
        {
            if (this.getInRepeatingMode().equals(RepeatMode.ALL))
            {
                this.currentTrackInPlaybackIndex = 0;
                this.skipToNext();
                this.play();
                return;
            }
            //TODO: check if nedofmedia sets stop
            seekBeginning();
            return;
        }
        this.currentTrackInPlaybackIndex++;
        this.skipToNext();
        this.play();
    }
////////////////////////////////////////////////////////////////////////////////
//  Properties and classes                                                    //
////////////////////////////////////////////////////////////////////////////////

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
