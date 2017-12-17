package hsrm.eibo.mediaplayer.Core.Controller;

import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.M3uParserTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.util.*;

public class PlaylistManager extends ArrayList<Playlist>{
    private static final String M3U_PARSER_THREAD_NAME = "M3U Parser Thread";
    private static PlaylistManager instance = new PlaylistManager();
    private static ObservableList<Playlist> observableInstance = FXCollections.observableArrayList(instance);
    private Playlist lastAddedPlaylist;

    private static ArrayList<PlaylistManagerObserver> observers = new ArrayList<>();

    public static void addOnChangeObserver(PlaylistManagerObserver obj)
    {
        observers.add(obj);
    }
    public static void removeOnChangeObserver(PlaylistManagerObserver obj)
    {
        observers.remove(obj);
    }

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
                } catch (Exception e) {
                    ErrorHandler.getInstance().addError(e);
                }
            }
        });
        //TODO: throw Exceptions to View
        parser.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                setIsLoadingList(false);
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
