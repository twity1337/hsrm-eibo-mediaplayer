package hsrm.eibo.mediaplayer.Core.Controller;

import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.M3uParserTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.util.*;

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
     * Method to sign up an Observer object
     * @param obj observer to notify on change
     */
    public static void addOnChangeObserver(PlaylistManagerObserver obj)
    {
        observers.add(obj);
    }

    /**
     * Method to notify all known observers
     */
    private void notifyOnChangeObservers()
    {
        PlaylistManager ths = this;
        observers.forEach(observer -> observer.update(ths));
    }

    public static PlaylistManager getInstance() {
        return instance;
    }

    private PlaylistManager(){this.isLoadingList = new SimpleBooleanProperty(false);}

    public void createPlaylistFromFile(File playlistFile)
    {
        this.setIsLoadingList(true);
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
        parser.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                setIsLoadingList(false);
                ErrorHandler err = ErrorHandler.getInstance();
                err.addError(event.getSource().getException());
                err.notifyErrorObserver("Fehler beim Laden der Metadaten");
            }
        });
        Thread parserThread = new Thread(parser, M3U_PARSER_THREAD_NAME);
        parserThread.setDaemon(true);
        parserThread.start();
    }

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

    public void savePlaylistToFile(Playlist playlist){
        //TODO: fill me i'm a stub
    }

    public Playlist getPlaylist(String name)
    {
        for (Playlist playlist:this){
            if(playlist.getName().equals(name))
                return playlist;
        }
        return null;
    }

    public Playlist getLastAddedPlaylist() {
        return lastAddedPlaylist;
    }

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
     *
     * @param playlist
     * @param tracksToAd
     * @throws PlaylistIOException
     */
    public void addTrackToPlaylist(Playlist playlist, Track...tracksToAd)
            throws PlaylistIOException
    {
        playlist.addAll(Arrays.asList(tracksToAd));
        notifyOnChangeObservers();
    }

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
