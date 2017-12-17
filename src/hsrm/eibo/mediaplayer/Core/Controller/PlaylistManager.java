package hsrm.eibo.mediaplayer.Core.Controller;

import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.M3uParserTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PlaylistManager is handling loading playlists from file(s), retaining them for furthrt use,
 * handing palylists over to other instances and (possibly) saving them to a playlist file.
 * PlaylistManager is a Singleton.
 */
public class PlaylistManager extends ArrayList<Playlist>{
    /**
     * name of parsinf plalyist to media file list
     */
    private static final String M3U_PARSER_THREAD_NAME = "M3U Parser Thread";
    /**
     * reference to singleton instances of PlaylistManager
     */
    private static PlaylistManager instance = new PlaylistManager();
    /**
     * reference to last added playlist
     */
    private Playlist lastAddedPlaylist;
    /**
     * list containing all active observers on PlaylistManager instance;
     */
    private static ArrayList<PlaylistManagerObserver> observers = new ArrayList<>();

    /**
     * Binds an onChange observer to this class.
     * @param obj the Observer
     */
    public static void addOnChangeObserver(PlaylistManagerObserver obj)
    {
        observers.add(obj);
    }
    /**
     * Binds an onChange observer to this class.
     * @param obj the Observer
     */
    public static void removeOnChangeObserver(PlaylistManagerObserver obj)
    {
        observers.remove(obj);
    }

    /**
     * notify all known observers
     */
    private void notifyOnChangeObservers()
    {
        PlaylistManager ths = this;
        observers.forEach(observer -> observer.update(ths));
    }

    /**
     * Method to access PlaylistManager instance
     * @return Singleton instance of PlaylistManager
     */
    public static PlaylistManager getInstance() {
        return instance;
    }

    /**
     * Constructor private to create only one instance of PlaylistManager
     */
    private PlaylistManager(){this.isLoadingList = new SimpleBooleanProperty(false);}

    /**
     * Method to create playlist from playlist file
     * @see Playlist
     * @param playlistFile File to load
     */
    public void createPlaylistFromFile(File playlistFile)
    {
        this.setIsLoadingList(true);
        //instantiate Parser of playlist file in new thread
        M3uParserTask parser = new M3uParserTask(playlistFile);
        parser.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public synchronized void handle(WorkerStateEvent event) {
                try {
                    instance.add(
                    lastAddedPlaylist = new Playlist(playlistFile.getPath(), (String[])event.getSource().getValue()));
                    setIsLoadingList(false);
                    notifyOnChangeObservers();
                    event.getSource().cancel();
                } catch (Exception e) {
                    ErrorHandler err = ErrorHandler.getInstance();
                    err.addError(e);
                    err.notifyErrorObserver("Fehler beim Laden der Playlist");
                }
            }
        });
        //TODO: throw Exceptions to View
        parser.setOnFailed(event -> {
            setIsLoadingList(false);
            ErrorHandler err = ErrorHandler.getInstance();
            err.addError(event.getSource().getException());
            err.notifyErrorObserver("Fehler beim Laden der Metadaten");
        });
        Thread parserThread = new Thread(parser, M3uParserTask.M3U_PARSER_THREAD_NAME);
        parserThread.setDaemon(true);
        parserThread.start();
    }

    /**
     * Method to load playlist with track files as content
     * @param trackFiles Listr of track files to load
     * @throws PlaylistIOException on file IO error
     */
    public void createPlaylistFromFile(List<File> trackFiles) throws PlaylistIOException
    {
        int trackFileSize = trackFiles.size();
        String[] trackPaths = new String[trackFileSize];
        for(int i = 0; i < trackFileSize; i++)
        {
            trackPaths[i] = trackFiles.get(i).getPath();
        }

        try {
            lastAddedPlaylist = new Playlist(trackPaths);
        } catch (Exception e)
        {

        }
        lastAddedPlaylist.setName("Einzelne Dateien");

        instance.add(lastAddedPlaylist);
        notifyOnChangeObservers();
    }

    /**
     * Method to get playlist by its name
     * @param name of playlist to return
     * @return Playlist with given name, null if not existent
     */
    public Playlist getPlaylist(String name)
    {
        for (Playlist playlist:this){
            if(playlist.getName().equals(name))
                return playlist;
        }
        return null;
    }

    /**
     * method to get reference to last loaded playlist
     * @return last loaded playlist object
     */
    public Playlist getLastAddedPlaylist() {
        return lastAddedPlaylist;
    }

    /**
     * Property to indicate status: true if currently loading a playlist, false if not
     */
    private SimpleBooleanProperty isLoadingList;

    public boolean isIsLoadingList() {
        return isLoadingList.get();
    }

    public SimpleBooleanProperty isLoadingListProperty() {
        return isLoadingList;
    }

    public void setIsLoadingList(boolean isLoadingList) {
        this.isLoadingList.set(isLoadingList);
    }

    /**
     * Method to add track object(s) to given specific playlist
     * @param playlist tracks should be added to
     * @param tracksToAd to add to playlist
     * @throws PlaylistIOException on media file IO error
     */
    public void addTrackToPlaylist(Playlist playlist, Track...tracksToAd)
            throws PlaylistIOException
    {
        playlist.addAll(Arrays.asList(tracksToAd));
        notifyOnChangeObservers();
    }

    /**
     *
     * @param playlist
     * @param trackIndex
     * @return
     */
    public int getAbsoluteIndexForAllPlaylists(Playlist playlist, int trackIndex)
    {
        Playlist[] list = super.toArray(new  Playlist[]{});
        int passedIndices = 0;
        for(Playlist pl : list)
        {
            if(pl.equals(playlist))
                break;
            passedIndices += pl.size()+1;
        }
        return passedIndices + trackIndex + 1;
    }
}
