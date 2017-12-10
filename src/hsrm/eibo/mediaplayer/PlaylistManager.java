package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Util.M3uParserTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.util.ArrayList;

public class PlaylistManager extends ArrayList<Playlist>{
    private static final String M3U_PARSER_THREAD_NAME = "M3U Parser Thread";
    private static PlaylistManager instance = new PlaylistManager();
    private static ObservableList<Playlist> observableInstance = FXCollections.observableArrayList(instance);
    private Playlist lastAddedPlaylist;

    public static PlaylistManager getInstance() {
        return instance;
    }

    private PlaylistManager(){this.isLoadingList = new SimpleBooleanProperty(false);}

    public void loadPlaylistFromFile(File playlistFile) throws PlaylistIOException
    {
        this.setIsLoadingList(true);
        M3uParserTask parser = new M3uParserTask(playlistFile);
        parser.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    instance.add(
                    lastAddedPlaylist = new Playlist((String[])event.getSource().getValue()));
                    setIsLoadingList(false);
                } catch (PlaylistIOException e) {
                    e.printStackTrace();
                }
            }
        });
        //TODO: setOnfailed
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
}
