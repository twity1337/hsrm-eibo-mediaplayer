package hsrm.eibo.mediaplayer.Core.Controller;
import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.beans.property.*;

import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaController {
    private int[] shuffleList;
    private int currentPlaybackIndex;
    private int currentPlaylistIndex; //TODO: todo
    private MediaPlayer currentMediaplayer;
    private Playlist playlist;
    private static MediaController instance = new MediaController();

    public static MediaController getInstance() {
        return instance;
    }

    private MediaController() {
        //TODO: load config on startup(maybe)
        this.currentPlaybackIndex = 0;
        trackDuration = new SimpleDoubleProperty(0);
        inShuffleMode = new SimpleBooleanProperty(false);
        repeatMode = new SimpleObjectProperty<>(RepeatMode.NONE);
        playing = new SimpleBooleanProperty(false);
        stopped = new SimpleBooleanProperty(false);
        endOfMedia = new SimpleBooleanProperty(true);
        volume = new SimpleDoubleProperty(0.5);
        currentTime = new SimpleDoubleProperty(0);
        coverProperty = new SimpleObjectProperty<>(null);
        toggledShuffle = new SimpleBooleanProperty(false);
        currentTrackMetadata = new SimpleObjectProperty<>();
        muted = new SimpleBooleanProperty(false);
    }


    public void play()
    {
        if (this.currentMediaplayer == null  || this.currentMediaplayer.getStatus() == MediaPlayer.Status.HALTED)
        {
            // TODO Throw error
            return;
        }
        if(this.currentMediaplayer.getStatus() == MediaPlayer.Status.UNKNOWN)
        {
            this.currentMediaplayer.statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
                @Override
                public void changed(ObservableValue<? extends MediaPlayer.Status> observable, MediaPlayer.Status oldValue, MediaPlayer.Status newValue) {
                    if (newValue != MediaPlayer.Status.READY)
                        return;
                    instance.play();
                }
            });

        }

        this.currentMediaplayer.play();
        this.setPlaying(true);
    }

    public void pause()
    {
        if (this.currentMediaplayer != null)
            this.currentMediaplayer.pause();
        this.setPlaying(false);
    }

    public void skipToNext()
    {
        if (!mediaplayerSet())
            return;
        if (!isEndOfPlaylist())
        {
            this.currentPlaybackIndex++;
        }
        else if (this.getRepeatMode().equals(RepeatMode.ALL) && this.isEndOfPlaylist())
        {
            this.currentPlaybackIndex = 0;
        }
        this.setCurrentMediaplayer();
    }

    public void skipToPrevious()
    {
        if (!mediaplayerSet())
            return;
        if (this.currentPlaybackIndex >0)
            currentPlaybackIndex--;
        this.setCurrentMediaplayer();
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
        this.playlist = playlist;
        this.currentPlaybackIndex = 0;
        this.setCurrentMediaplayer();
        System.out.println("Playlist: " + playlist.getLocation());
    }

    /**
     * method to set Mediaplayer in MediaController
     * @param track from where MediaPlayer objekt is extracted
     */
    public void setCurrentMediaplayer(Track track)
    {
        //stopping old mediaplayer and cleanup
        if(this.currentMediaplayer != null)
            this.currentMediaplayer.dispose();
        this.setPlaying(false);

        // setting new mediaplayer and its eventhandler
        this.currentMediaplayer = track.getTrackMediaPlayer();
        bindListenersToCurrentMediaPlayer();
        System.out.println("Track: " + track.getLocation());
    }

    private boolean mediaplayerSet(){return this.currentMediaplayer != null;}

    private void setCurrentMediaplayer()
    {
        this.setCurrentMediaplayer(
                this.playlist.get(currentPlaybackIndex));
    }

    /**
     * method to determine current track played in playlist
     * @return list position in playlist of track currently played/selected
     */
    public int getCurrentPlaylistIndex()
    {
        if (getToggledShuffle())
            return shuffleList[currentPlaybackIndex];
        return currentPlaybackIndex;
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
        if (this.getRepeatMode().equals(RepeatMode.SINGLE))
        {
            this.rewind();
            this.play();
        } else {
            this.skipToNext();
            this.currentMediaplayer.setOnReady(this::play);
        }
    }

    private boolean isEndOfPlaylist()
    {
        return (this.currentPlaybackIndex >= this.playlist.size()-1);
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
            instance.setEndOfMedia(true);
            currentMediaplayer.stop();
            instance.playNext();
        });

        this.currentMediaplayer.setOnReady(() -> {
            instance.coverProperty.set((Image)currentMediaplayer.getMedia().getMetadata().get("image"));
            instance.trackDurationProperty().set(currentMediaplayer.getTotalDuration().toSeconds());
        });
        //bind volume property bidirectional
        this.currentMediaplayer.volumeProperty().
                bindBidirectional(this.volumeProperty());
        // currentTime(double) listens to mediaplayer.currentTimeProperty(ReadOnlyObjectProperty<Duration>)
        this.currentMediaplayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> currentTimeProperty().set(newValue.toSeconds()));

        this.currentMediaplayer.muteProperty().bindBidirectional(this.muted);
    }
////////////////////////////////////////////////////////////////////////////////
//  Properties and classes                                                    //
////////////////////////////////////////////////////////////////////////////////

 /* TODO:  *//**
     * returns double (in seconds)
     *//*
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
    }*/

    private SimpleObjectProperty<Image> coverProperty;

    public SimpleObjectProperty<Image> coverProperty(){return coverProperty;}

    public void setCoverProperty(Image coverProperty) {
        this.coverProperty.set(coverProperty);
    }

    private Image getCover(){return coverProperty.get();}

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

    private SimpleBooleanProperty toggledShuffle;

    public boolean getToggledShuffle() {
        return toggledShuffle.get();
    }

    public SimpleBooleanProperty toggledShuffleProperty() {
        return toggledShuffle;
    }

    public void setToggledShuffle(boolean toggledShuffle) {
        this.toggledShuffle.set(toggledShuffle);
        //ON->OFF
        //if replay gets unshuffeld playlist continues to play
        // tracks in order(continuing track currently playing)
        if(this.getToggledShuffle())
        {
            this.currentPlaybackIndex = this.shuffleList[this.currentPlaybackIndex];
            return;
        }
        //OFF->ON
        //if replay of tracks gets shuffled
        //playlist wont remember tracks already played
        //shuffled = !shuffled;
        createShuffleList();
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

    private enum RepeatMode {NONE,SINGLE,ALL}

    private SimpleObjectProperty<RepeatMode> repeatMode;

    public RepeatMode getRepeatMode() {
        return repeatMode.get();
    }

    public SimpleObjectProperty<RepeatMode> inRepeatingModeProperty() {
        return repeatMode;
    }

    public void setInRepeatingMode(RepeatMode inRepeatingMode) {
        this.repeatMode.set(inRepeatingMode);
    }

    private DoubleProperty trackDuration;

    public double getTrackDuration() {
        return trackDuration.get();
    }

    public DoubleProperty trackDurationProperty() {
        return trackDuration;
    }

    public void setTrackDuration(double trackDuration) {
        this.trackDuration.set(trackDuration);
    }

    private SimpleObjectProperty<Metadata> currentTrackMetadata;

    public Metadata getCurrentTrackMetadata() {
        return currentTrackMetadata.get();
    }

    public SimpleObjectProperty<Metadata> currentTrackMetadataProperty() {
        return currentTrackMetadata;
    }

    public void setCurrentTrackMetadata(Metadata currentTrackMetadata) {
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
}
